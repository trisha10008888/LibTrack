
package com.libtrack.backend;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(originPatterns = {
    "http://localhost:5173",
    "https://*.vercel.app"
})
public class BookRequestController {

    private final BookRequestService requestService;

    public BookRequestController(BookRequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public List<BookRequest> getAllRequests() {
        return requestService.getAllRequests();
    }

    @GetMapping("/{id}")
    public BookRequest getRequestById(@PathVariable Long id) {
        return requestService.getRequestById(id);
    }

    @PostMapping("/book/{bookId}")
    public BookRequest createRequest(@PathVariable Long bookId) {
        return requestService.createRequest(bookId);
    }

    @PutMapping("/{id}/status")
    public BookRequest updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        return requestService.updateStatus(id, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        requestService.deleteRequest(id);
        return ResponseEntity.noContent().build();
    }
}
