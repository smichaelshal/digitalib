package org.plasma.digitalib.filters;

import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class AllBookFilterTest {

    @Test
    void test_withBook_shouldReturnTrue() {
        // Arrange
        AllBookFilter filter = new AllBookFilter();
        Book book = mock(Book.class);

        // Act
        boolean testResult = filter.test(book);

        // Assert
        assertTrue(testResult);
    }
}