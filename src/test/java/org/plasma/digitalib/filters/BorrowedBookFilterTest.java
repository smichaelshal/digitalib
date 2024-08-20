package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

class BorrowedBookFilterTest {
    private BorrowedBookFilter filter;
    private Book book;

    @BeforeEach
    void setup() {
        this.filter = new BorrowedBookFilter();
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    void test_withPresentBook_shouldReturnFalse() {
        // Arrange

        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertFalse(testResult);
    }

    @Test
    void test_withBorrowedBook_shouldReturnTrue() {
        // Arrange
        this.book.setIsBorrowed(true);

        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertTrue(testResult);
    }
}