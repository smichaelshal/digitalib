package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import java.time.Instant;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BookIdentifierFilterTest {
    private BookIdentifierFilter filter;
    private Book book;
    private BookIdentifier bookIdentifier;

    @BeforeEach
    void setup() {
        this.bookIdentifier = new BookIdentifier("name", "author");
        this.book = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "genre",
                "summary",
                this.bookIdentifier);
        this.filter = new BookIdentifierFilter(this.book.getBookIdentifier());
    }

    @Test
    void test_withEqualBookIdentifiers_shouldReturnTrue() {
        // Arrange
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName(),
                        this.bookIdentifier.getAuthor()));


        // Act
        boolean filterResult = this.filter.test(secondBook);

        // Assert
        assertTrue(filterResult);
    }

    @Test
    void test_withDifferentNames_shouldReturnFalse() {
        // Arrange
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName() + "_",
                        this.bookIdentifier.getAuthor()));

        // Act
        boolean filterResult = this.filter.test(secondBook);

        // Assert
        assertFalse(filterResult);
    }

    @Test
    void test_withDifferentAuthors_shouldReturnFalse() {
        // Arrange
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName(),
                        this.bookIdentifier.getAuthor() + "_"));

        // Act
        boolean filterResult = this.filter.test(secondBook);

        // Assert
        assertFalse(filterResult);
    }

    @Test
    void test_withDifferentNamesAndAuthors_shouldReturnFalse() {
        // Arrange
        Book secondBook = new Book(
                new LinkedList<>(),
                UUID.randomUUID(),
                Instant.now(),
                false,
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName() + "_",
                        this.bookIdentifier.getAuthor() + "_"));

        // Act
        boolean filterResult = this.filter.test(secondBook);

        // Assert
        assertFalse(filterResult);
    }
}