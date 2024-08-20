package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PresentBookFilterTest {
    private PresentBookFilter filter;
    private Book book;

    @BeforeEach
    void setup() {
        this.filter = new PresentBookFilter();
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    void test_withPresentBook_shouldReturnTrue() {
        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertTrue(testResult);
    }

    @Test
    void test_withBorrowedBook_shouldReturnFalse() {
        // Arrange
        this.book.setIsBorrowed(true);

        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertFalse(testResult);
    }
}