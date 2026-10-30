
import { useState, useEffect } from 'react'
import './App.css'
import {
  getAllBooks,
  getAllRequests,
  createBookRequest,
  deleteBookRequest,
  updateRequestStatus,
} from './services/bookService'

const categories = [
  'All books',
  'Personal Development, Motivation & Psychology',
  'Fiction',
  'Mystery',
  'Horror & Thriller',
  'Science Fiction',
  'Biography & Autobiography',
  'Business & Finance',
  'History',
]

const categoryMap = {
  'Personal Development, Motivation & Psychology': 'Personal Development',
  'Science Fiction': 'Sci-Fi',
  'Biography & Autobiography': 'Biography',
  'Business & Finance': 'Business',
}

const coverColors = [
  'cover-rose', 'cover-sage', 'cover-blue', 'cover-cream',
  'cover-dark', 'cover-pink', 'cover-brown', 'cover-green',
]

const labels = [
  'Reader favourite', 'Popular', 'Staff pick', 'Bestseller',
  'Must read', 'Classic', 'Book club pick', 'Recommended',
]

const heights = ['tall', 'short', 'medium']

function getSavedData(key, fallback) {
  try {
    const savedData = localStorage.getItem(key)
    return savedData ? JSON.parse(savedData) : fallback
  } catch {
    return fallback
  }
}

function formatDate(date) {
  if (!date) return null

  return new Date(`${date}T00:00:00`).toLocaleDateString('en-IN', {
    day: 'numeric',
    month: 'short',
    year: 'numeric',
  })
}

function App() {
  const [books, setBooks] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const [activeCategory, setActiveCategory] = useState('All books')
  const [search, setSearch] = useState('')
  const [requestedBooks, setRequestedBooks] = useState([])

  const [readingList, setReadingList] = useState(() =>
    getSavedData('libtrack_reading_list', [])
  )

  const [activePage, setActivePage] = useState('Discover')

  useEffect(() => {
    const loadLibrary = async () => {
      try {
        setLoading(true)
        setError('')

        const [bookData, requestData] = await Promise.all([
          getAllBooks(),
          getAllRequests(),
        ])

        setBooks(bookData.map((book, index) => ({
          ...book,
          color: coverColors[index % coverColors.length],
          label: labels[index % labels.length],
          height: heights[index % heights.length],
        })))

        setRequestedBooks(requestData)
      } catch (err) {
        console.error(err)
        setError('We could not connect to your library. Please check that the backend is running.')
      } finally {
        setLoading(false)
      }
    }

    loadLibrary()
  }, [])

  useEffect(() => {
    localStorage.setItem(
      'libtrack_reading_list',
      JSON.stringify(readingList)
    )
  }, [readingList])

  const getRequest = (bookId) =>
    requestedBooks.find((request) => request.book?.id === bookId)

  const handleRequest = async (bookId) => {
    const existingRequest = getRequest(bookId)

    try {
      if (existingRequest) {
        if (existingRequest.status !== 'Pending') return

        await deleteBookRequest(existingRequest.id)

        setRequestedBooks((previous) =>
          previous.filter((request) => request.id !== existingRequest.id)
        )
      } else {
        const newRequest = await createBookRequest(bookId)
        setRequestedBooks((previous) => [...previous, newRequest])
      }
    } catch (err) {
      console.error(err)
      alert(err.message || 'Something went wrong.')
    }
  }

  const handleStatusUpdate = async (requestId, status) => {
    try {
      const updatedRequest = await updateRequestStatus(requestId, status)

      setRequestedBooks((previous) =>
        previous.map((request) =>
          request.id === requestId ? updatedRequest : request
        )
      )

      // Keep the catalogue availability in sync with the backend response.
      if (updatedRequest.book) {
        setBooks((previous) =>
          previous.map((book) =>
            book.id === updatedRequest.book.id
              ? { ...book, available: updatedRequest.book.available }
              : book
          )
        )
      }
    } catch (err) {
      console.error(err)
      alert(err.message || 'Unable to update request.')
    }
  }

  const handleReadingList = (id) => {
    setReadingList((previous) =>
      previous.includes(id)
        ? previous.filter((bookId) => bookId !== id)
        : [...previous, id]
    )
  }

  const filteredBooks = books.filter((book) => {
    const matchesCategory =
      activeCategory === 'All books' ||
      book.category === (categoryMap[activeCategory] || activeCategory)

    const matchesSearch =
      book.title.toLowerCase().includes(search.toLowerCase()) ||
      book.author.toLowerCase().includes(search.toLowerCase())

    const request = getRequest(book.id)

    const matchesPage =
      activePage === 'My requests'
        ? Boolean(request)
        : activePage === 'Issued books'
          ? request?.status === 'Issued' || request?.status === 'Overdue'
          : activePage === 'My reading list'
            ? readingList.includes(book.id)
            : true

    return matchesCategory && matchesSearch && matchesPage
  })

  const pageTitle = {
    Discover: 'Explore the collection',
    'My requests': 'Your requested books',
    'Issued books': 'Your borrowed books',
    'My reading list': 'Your reading list',
  }

  return (
    <div className="app-shell">

      <aside className="sidebar">
        <div className="brand">
          <div className="brand-icon">L.</div>
          <div>
            <h1>libtrack</h1>
            <span>THE LITTLE BOOK NOOK</span>
          </div>
        </div>

        <div className="sidebar-label">MENU</div>

        <nav className="side-nav">
          {[
            ['Discover', '⌂'],
            ['My requests', '♡'],
            ['Issued books', '▤'],
            ['My reading list', '☆'],
          ].map(([name, icon]) => (
            <button
              key={name}
              className={`nav-item ${activePage === name ? 'active' : ''}`}
              onClick={() => {
                setActivePage(name)
                setActiveCategory('All books')
                setSearch('')
              }}
            >
              <span className="nav-icon">{icon}</span>
              {name}

              {name === 'My requests' && requestedBooks.length > 0 && (
                <span className="nav-count">{requestedBooks.length}</span>
              )}
            </button>
          ))}
        </nav>

        <div className="sidebar-note">
          <span className="note-flower">✿</span>
          <p>A little reading every day goes a long way.</p>
          <span>YOUR NEXT CHAPTER AWAITS.</span>
        </div>

        <div className="profile">
          <div className="avatar">L</div>
          <div>
            <strong>Book Lover</strong>
            <span>Library member</span>
          </div>
          <span className="profile-dots">···</span>
        </div>
      </aside>

      <main className="main-content">

        <header className="topbar">
          <div className="breadcrumb">
            HOME <span>/</span> {activePage.toUpperCase()}
          </div>

          <div className="topbar-right">
            <span className="open-status">
              <span></span> YOUR LITTLE LIBRARY
            </span>
            <div className="top-avatar">L</div>
          </div>
        </header>

        <section className="welcome-section">
          <div className="welcome-copy">
            <span className="eyebrow">A WORLD BETWEEN THE PAGES</span>
            <h2>
              Find your next
              <br />
              <em>favourite chapter.</em>
            </h2>
            <p>
              A cosy corner to discover books, request your
              favourites and keep your reading journey organised.
            </p>
          </div>

          <div className="welcome-decoration">
            <span className="deco-star">✳</span>
            <div className="book-stack">
              <div className="stack-book stack-one"></div>
              <div className="stack-book stack-two"></div>
              <div className="stack-book stack-three"></div>
            </div>
            <span className="deco-text">stay curious, always.</span>
          </div>
        </section>

        <section className="stats-row">
          <div className="stat-card">
            <span className="stat-icon">▤</span>
            <div>
              <strong>{books.length.toString().padStart(2, '0')}</strong>
              <span>Books to discover</span>
            </div>
          </div>

          <div className="stat-card">
            <span className="stat-icon">♡</span>
            <div>
              <strong>{requestedBooks.length.toString().padStart(2, '0')}</strong>
              <span>My requests</span>
            </div>
          </div>

          <div className="stat-card">
            <span className="stat-icon">✿</span>
            <div>
              <strong>{categories.length - 1}</strong>
              <span>Book categories</span>
            </div>
          </div>
        </section>

        <section className="catalogue-section">

          <div className="section-heading">
            <div>
              <span className="eyebrow">A SHELF FULL OF POSSIBILITIES</span>
              <h2>{pageTitle[activePage]}</h2>
              <p>
                {activePage === 'My requests'
                  ? 'Keep an eye on the books you have requested.'
                  : activePage === 'Issued books'
                    ? 'Your currently borrowed books.'
                    : activePage === 'My reading list'
                      ? 'Books saved for another day.'
                      : 'Little discoveries, one book at a time.'}
              </p>
            </div>

            <span className="book-total">
              {loading ? 'LOADING...' : `${filteredBooks.length} BOOKS`}
            </span>
          </div>

          <div className="search-box">
            <span>⌕</span>
            <input
              type="text"
              placeholder="Search by book title or author..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
            <span className="search-hint">EXPLORE</span>
          </div>

          <div className="category-list">
            {categories.map((category) => (
              <button
                key={category}
                className={`category-chip ${activeCategory === category ? 'selected' : ''}`}
                onClick={() => setActiveCategory(category)}
              >
                {category}
              </button>
            ))}
          </div>

          <div className="book-grid">

            {loading && (
              <div className="empty-state">
                <span>✿</span>
                <h3>Opening your little library...</h3>
                <p>Gathering your books from the database.</p>
              </div>
            )}

            {!loading && error && (
              <div className="empty-state">
                <span>✿</span>
                <h3>Something went wrong.</h3>
                <p>{error}</p>
              </div>
            )}

            {!loading && !error && filteredBooks.map((book) => {
              const request = getRequest(book.id)
              const isSaved = readingList.includes(book.id)

              return (
                <article className="book-card" key={book.id}>
                  <div className={`book-cover ${book.color} ${book.height}`}>
                    <span className="cover-label">{book.label}</span>
                    <span className="cover-decoration">✳</span>
                    <span className="cover-title">{book.title}</span>
                    <span className="cover-author">{book.author}</span>
                    <span className="cover-publisher">LIBTRACK COLLECTION</span>
                  </div>

                  <div className="book-details">
                    <div>
                      <span className="book-category">{book.category}</span>
                      <h3>{book.title}</h3>
                      <p>by {book.author}</p>

                      {request && (
                        <div className="request-status">
                          <span className={`status-dot ${request.status.toLowerCase()}`}></span>
                          {request.status}
                        </div>
                      )}

                      {request && (
                        <div className="borrowing-dates">
                          {request.requestedAt && (
                            <p>Requested: {new Date(request.requestedAt).toLocaleDateString('en-IN')}</p>
                          )}
                          {request.issueDate && (
                            <p>Issued: {formatDate(request.issueDate)}</p>
                          )}
                          {request.dueDate && (
                            <p className={request.status === 'Overdue' ? 'overdue-date' : ''}>
                              Due: {formatDate(request.dueDate)}
                            </p>
                          )}
                          {request.returnDate && (
                            <p>Returned: {formatDate(request.returnDate)}</p>
                          )}
                        </div>
                      )}

                      {request && activePage === 'My requests' && (
                        <div className="admin-actions">
                          {request.status === 'Pending' && (
                            <button onClick={() => handleStatusUpdate(request.id, 'Approved')}>
                              Approve request
                            </button>
                          )}
                          {request.status === 'Approved' && (
                            <button onClick={() => handleStatusUpdate(request.id, 'Issued')}>
                              Issue book
                            </button>
                          )}
                          {(request.status === 'Issued' || request.status === 'Overdue') && (
                            <button onClick={() => handleStatusUpdate(request.id, 'Returned')}>
                              Mark as returned
                            </button>
                          )}
                        </div>
                      )}
                    </div>

                    <button
                      className={`request-button ${request ? 'requested' : ''}`}
                      onClick={() => handleRequest(book.id)}
                      disabled={request && request.status !== 'Pending'}
                    >
                      {request
                        ? request.status === 'Pending'
                          ? 'Cancel request'
                          : request.status
                        : '♡ Request'}
                    </button>

                    <button
                      className={`reading-list-button ${isSaved ? 'saved' : ''}`}
                      onClick={() => handleReadingList(book.id)}
                    >
                      {isSaved ? '★ Saved to reading list' : '☆ Add to reading list'}
                    </button>
                  </div>
                </article>
              )
            })}

            {!loading && !error && filteredBooks.length === 0 && (
              <div className="empty-state">
                <span>✿</span>
                <h3>
                  {activePage === 'My requests'
                    ? 'No requests just yet.'
                    : activePage === 'Issued books'
                      ? 'No books issued yet.'
                      : activePage === 'My reading list'
                        ? 'Your reading list is empty.'
                        : 'No books found just yet.'}
                </h3>
                <p>
                  {activePage === 'My requests'
                    ? 'Explore the collection and request your next read.'
                    : activePage === 'Issued books'
                      ? 'Your issued books will appear here.'
                      : activePage === 'My reading list'
                        ? 'Save books you would love to read later.'
                        : 'Try another search or explore a different category.'}
                </p>
              </div>
            )}

          </div>
        </section>

        <footer className="footer">
          <span>MADE WITH ♡ FOR BOOK LOVERS</span>
          <span>LIBTRACK © 2026</span>
        </footer>

      </main>
    </div>
  )
}

export default App
