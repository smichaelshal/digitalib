package org.plasma.digitalib.tasks;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.searchers.Searcher;

import java.util.function.Predicate;

import static org.mockito.Mockito.verify;

class PrintBooksByFilterTaskTest {
    @Mock
    private Searcher<Book> searcher;

    @Mock
    private Predicate<Book> filter;

    @Test
    void run_withFilter_shouldCallSearch() {
        // Arrange
        MockitoAnnotations.initMocks(this);
        Task task =
                new PrintBooksByFilterTask("print books by filter", searcher,
                        filter);

        // Act
        task.run();

        // Assert
        verify(searcher).search(filter);
    }
}