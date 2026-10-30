
package com.libtrack.backend;

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BackendApplicationTests {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private Validator validator;

    // Test 1: Check whether Spring Boot starts successfully
    @Test
    void contextLoads() {
        assertNotNull(bookRepository);
    }

    // Test 2: Check whether the database contains the seeded books
    @Test
    void bookCatalogueContainsSeededBooks() {
        long totalBooks = bookRepository.count();

        assertTrue(
            totalBooks >= 75,
            "The catalogue should contain at least 75 books."
        );
    }

    // Test 3: Check whether empty book details are rejected
    @Test
    void bookValidationRejectsEmptyFields() {

        Book book = new Book();

        book.setTitle("");
        book.setAuthor("");
        book.setCategory("");

        Set<ConstraintViolation<Book>> violations =
                validator.validate(book);

        assertEquals(3, violations.size());
    }
}
