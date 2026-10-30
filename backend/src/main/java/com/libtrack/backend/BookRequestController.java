
package com.libtrack.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:5173")
public class BookRequestController {

    private final BookRequestService requestService;

    public BookRequestController(BookRequestService requestService) {
        this.requestService = requestService;
    }

    // Get all requests
    @GetMapping
    public List<BookRequest> getAllRequests() {
        return requestService.getAllRequests();
    }

    // Get request by ID
    @GetMapping("/{id}")
    public BookRequest getRequestById(@PathVariable Long id) {
        return requestService.getRequestById(id);
    }

    // Create a request for a book
    @PostMapping("/book/{bookId}")
    public BookRequest createRequest(@PathVariable Long bookId) {
        return requestService.createRequest(bookId);
    }

    // Update request status
    @PutMapping("/{id}/status")
    public BookRequest updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return requestService.updateStatus(id, status);
    }

    // Delete a request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
