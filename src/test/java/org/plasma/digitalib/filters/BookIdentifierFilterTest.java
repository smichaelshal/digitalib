package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import static org.junit.jupiter.api.Assertions.*;

class BookIdentifierFilterTest {
    private BookIdentifierFilter filter;
    private Book book;
    private BookIdentifier bookIdentifier;

    @BeforeEach
    void setup() {
        this.bookIdentifier = new BookIdentifier("name", "author");
        this.book = new Book(
                "genre",
                "summary",
                this.bookIdentifier
        );
        this.filter = new BookIdentifierFilter(this.book.getBookIdentifier());
    }

    @Test
    void apply_withEqualBookIdentifiers_shouldReturnTrue() {
        // Arrange
        Book secondBook = new Book(
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName(),
                        this.bookIdentifier.getAuthor()));

        // Act
        boolean filterResult = this.filter.apply(secondBook);

        // Assert
        assertTrue(filterResult);
    }

    @Test
    void apply_withDifferentNames_shouldReturnTrue() {
        // Arrange
        Book secondBook = new Book(
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName() + "_",
                        this.bookIdentifier.getAuthor()));

        // Act
        boolean filterResult = this.filter.apply(secondBook);

        // Assert
        assertFalse(filterResult);
    }

    @Test
    void apply_withDifferentAuthors_shouldReturnTrue() {
        // Arrange
        Book secondBook = new Book(
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName(),
                        this.bookIdentifier.getAuthor() + "_"));

        // Act
        boolean filterResult = this.filter.apply(secondBook);

        // Assert
        assertFalse(filterResult);
    }

    @Test
    void apply_withDifferentNamesAndAuthors_shouldReturnTrue() {
        // Arrange
        Book secondBook = new Book(
                "genre",
                "summary",
                new BookIdentifier(
                        this.bookIdentifier.getName() + "_",
                        this.bookIdentifier.getAuthor() + "_"));

        // Act
        boolean filterResult = this.filter.apply(secondBook);

        // Assert
        assertFalse(filterResult);
    }
}