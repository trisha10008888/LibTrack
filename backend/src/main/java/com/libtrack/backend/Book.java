
package com.libtrack.backend;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Book title is required.")
    @Size(max = 150, message = "Book title cannot exceed 150 characters.")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Author name is required.")
    @Size(max = 100, message = "Author name cannot exceed 100 characters.")
    @Column(nullable = false)
    private String author;

    @NotBlank(message = "Book category is required.")
    @Column(nullable = false)
    private String category;

    private boolean available = true;

    public Book() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
