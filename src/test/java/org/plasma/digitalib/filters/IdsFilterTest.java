package org.plasma.digitalib.filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IdsFilterTest {
    private IdsFilter<Book> filter;
    private Book book;

    @BeforeEach
    void setup() {
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        this.filter = new IdsFilter<>(List.of(this.book.getId()));
    }

    @Test
    void test_withContainId_shouldReturnTrue() {
        // Act
        boolean filterResult = this.filter.test(this.book);

        // Assert
        assertTrue(filterResult);
    }

    @Test
    void test_withNotContainId_shouldReturnFalse() {
        // Arrange
        Book secondBook = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        // Act
        boolean filterResult = this.filter.test(secondBook);

        // Assert
        assertFalse(filterResult);
    }
}