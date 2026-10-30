
# LibTrack — The Little Book Nook

A Library Book Request and Issue Tracking System built using React, Spring Boot and PostgreSQL.

## Project Overview

LibTrack is a web-based library management application that allows users to explore a book catalogue, search for books, submit borrowing requests and track their reading and borrowing activity.

The system also provides librarian-style request management features for approving requests, issuing books, tracking due dates and processing returns.

## Technologies Used

### Frontend
- React.js
- JavaScript
- HTML5
- CSS3
- Vite

### Backend
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate
- Maven

### Database
- PostgreSQL
- pgAdmin 4

## Main Features

1. Browse a catalogue of 75+ books.
2. Search books by title and author.
3. Filter books by category.
4. Create and manage book requests.
5. Approve borrowing requests.
6. Issue books and calculate 14-day due dates.
7. Track issued, returned and overdue books.
8. Automatically identify overdue books.
9. Update book availability after issue and return.
10. Maintain a personal reading list.

## Database Design

The application uses two main relational entities:

### Book
- id
- title
- author
- category
- available

### BookRequest
- id
- book_id
- status
- requested_at
- issue_date
- due_date
- return_date

Relationship: One Book can have multiple BookRequest records. Each BookRequest belongs to one Book.

## Application Architecture

The project follows a layered backend architecture:

- Controller Layer: Handles REST API requests.
- Service Layer: Contains application business logic.
- Repository Layer: Handles database operations.
- Entity Layer: Defines database entities.

The React frontend communicates with the Spring Boot backend through REST APIs.

## REST API Endpoints

### Books

| Method | Endpoint | Purpose |
|---|---|---|
| GET | /api/books | Retrieve all books |
| GET | /api/books/{id} | Retrieve a book |
| POST | /api/books | Add a book |
| PUT | /api/books/{id} | Update a book |
| DELETE | /api/books/{id} | Delete a book |

### Book Requests

| Method | Endpoint | Purpose |
|---|---|---|
| GET | /api/requests | Retrieve all requests |
| GET | /api/requests/{id} | Retrieve a request |
| POST | /api/requests/book/{bookId} | Create a request |
| PUT | /api/requests/{id}/status | Update request status |
| DELETE | /api/requests/{id} | Delete a request |

## How to Run the Project

### Prerequisites
- Node.js and npm
- Java 21
- PostgreSQL
- Maven

### Backend Setup

1. Create a PostgreSQL database named `libtrack_db`.
2. Configure the database connection in `backend/src/main/resources/application.properties`.
3. Open a terminal and run:

```bash
cd backend
./mvnw spring-boot:run
```

Backend URL: http://localhost:8080

### Frontend Setup

Open another terminal:

```bash
cd frontend
npm install
npm run dev
```

Frontend URL: http://localhost:5173

## Testing

Automated backend tests cover:
- Spring Boot application context loading.
- Database catalogue availability.
- Book input validation.

Manual API testing covers CRUD operations for Books and Book Requests, along with the borrowing lifecycle and overdue detection.

## Project Status

Developed as a Full Stack Web Development academic project.

**Project Name:** LibTrack  
**Tagline:** The Little Book Nook
