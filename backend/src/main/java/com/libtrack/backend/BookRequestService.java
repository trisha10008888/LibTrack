
package com.libtrack.backend;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class BookRequestService {

    private final BookRequestRepository requestRepository;
    private final BookRepository bookRepository;

    public BookRequestService(
            BookRequestRepository requestRepository,
            BookRepository bookRepository) {
        this.requestRepository = requestRepository;
        this.bookRepository = bookRepository;
    }

    // Get all requests and automatically update overdue statuses
    public List<BookRequest> getAllRequests() {
        updateOverdueRequests();
        return requestRepository.findAll();
    }

    // Get a single request
    public BookRequest getRequestById(Long id) {
        BookRequest request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found: " + id));

        updateOverdueStatus(request);
        return request;
    }

    // Create a new book request
    public BookRequest createRequest(Long bookId) {

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() ->
                        new RuntimeException("Book not found: " + bookId));

        if (!book.isAvailable()) {
            throw new RuntimeException(
                    "This book is currently unavailable.");
        }

        BookRequest request = new BookRequest();
        request.setBook(book);
        request.setStatus("Pending");

        return requestRepository.save(request);
    }

    // Update request status
    public BookRequest updateStatus(Long id, String status) {

        List<String> validStatuses =
                List.of("Pending", "Approved", "Issued", "Returned");

        if (!validStatuses.contains(status)) {
            throw new IllegalArgumentException(
                    "Invalid request status.");
        }

        BookRequest request = getRequestById(id);

        // Approve a pending request
        if ("Approved".equals(status)) {

            if (!"Pending".equals(request.getStatus())) {
                throw new RuntimeException(
                        "Only pending requests can be approved.");
            }
        }

        // Issue an approved book
        if ("Issued".equals(status)) {

            if (!"Approved".equals(request.getStatus())) {
                throw new RuntimeException(
                        "Only approved requests can be issued.");
            }

            if (!request.getBook().isAvailable()) {
                throw new RuntimeException(
                        "This book is already issued.");
            }

            LocalDate today = LocalDate.now();

            request.setIssueDate(today);
            request.setDueDate(today.plusDays(14));
            request.setReturnDate(null);

            request.getBook().setAvailable(false);
            bookRepository.save(request.getBook());
        }

        // Return an issued or overdue book
        if ("Returned".equals(status)) {

            if (!"Issued".equals(request.getStatus())
                    && !"Overdue".equals(request.getStatus())) {

                throw new RuntimeException(
                        "Only issued or overdue books can be returned.");
            }

            // Support older requests created before date tracking
            request.setReturnDate(LocalDate.now());

            request.getBook().setAvailable(true);
            bookRepository.save(request.getBook());
        }

        request.setStatus(status);

        return requestRepository.save(request);
    }

    // Automatically identify overdue books
    private void updateOverdueStatus(BookRequest request) {

        if ("Issued".equals(request.getStatus())
                && request.getDueDate() != null
                && LocalDate.now().isAfter(request.getDueDate())) {

            request.setStatus("Overdue");
            requestRepository.save(request);
        }
    }

    // Check all requests for overdue books
    private void updateOverdueRequests() {

        List<BookRequest> requests = requestRepository.findAll();

        for (BookRequest request : requests) {
            updateOverdueStatus(request);
        }
    }

    // Delete a request
    public void deleteRequest(Long id) {

        BookRequest request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found: " + id));

        if ("Issued".equals(request.getStatus())
                || "Overdue".equals(request.getStatus())) {

            throw new RuntimeException(
                    "An issued or overdue book cannot be cancelled.");
        }

        requestRepository.delete(request);
    }
}
