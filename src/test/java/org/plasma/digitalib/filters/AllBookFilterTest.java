package org.plasma.digitalib.filters;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.plasma.digitalib.models.Book;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AllBookFilterTest {

    @Mock
    private Book book;

    @Test
    void test_withBook_shouldReturnTrue() {
        // Arrange
        AllBookFilter filter = new AllBookFilter();

        // Act
        boolean testResult = filter.test(book);

        // Assert
        assertTrue(testResult);
    }
}