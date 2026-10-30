
package com.libtrack.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRequestRepository
        extends JpaRepository<BookRequest, Long> {

    // Find all requests for a particular book
    List<BookRequest> findByBookId(Long bookId);

    // Find requests by their status
    List<BookRequest> findByStatus(String status);
}
