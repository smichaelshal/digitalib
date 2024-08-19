package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import static org.junit.jupiter.api.Assertions.*;

class BorrowingFilterTest {
    private BorrowingFilter<Book> filter;
    private Book book;

    @BeforeEach
    void setup() {
        this.filter = new BorrowingFilter<>();
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    void test_withPresentBook_shouldReturnFalse() {
        // Act
        boolean filterResult = this.filter.test(this.book);

        // Assert
        assertFalse(filterResult);
    }

    @Test
    void test_withBorrowedBook_shouldReturnTrue() {
        // Arrange
        this.book.setIsBorrowed(true);

        // Act
        boolean filterResult = this.filter.test(this.book);

        // Assert
        assertTrue(filterResult);
    }
}