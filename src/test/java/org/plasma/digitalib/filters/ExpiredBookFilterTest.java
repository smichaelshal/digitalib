package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.User;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpiredBookFilterTest {
    private ExpiredBookFilter filter;
    private Book book;

    @BeforeEach
    void setup() {
        this.filter = new ExpiredBookFilter();
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    void test_withEmptyBorrowingsBook_shouldReturnFalse() {
        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertFalse(testResult);
    }

    @Test
    void test_withBorrowingNotExpiredBook_shouldReturnFalse() {
        // Arrange
        Borrowing borrowing = new Borrowing(
                new User("1234"),
                Instant.now(),
                Instant.now().plus(1, ChronoUnit.DAYS));
        this.book.getBorrowings().add(borrowing);

        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertFalse(testResult);
    }

    @Test
    void test_withBorrowingExpiredBook_shouldReturnTrue() {
        // Arrange
        Borrowing borrowing = new Borrowing(
                new User("1234"),
                Instant.now(),
                Instant.now().minus(1, ChronoUnit.DAYS));
        this.book.getBorrowings().add(borrowing);

        // Act
        boolean testResult = this.filter.test(this.book);

        // Assert
        assertTrue(testResult);
    }
}