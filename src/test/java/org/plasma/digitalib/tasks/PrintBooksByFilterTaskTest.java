package org.plasma.digitalib.tasks;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.searchers.Searcher;

import java.time.format.DateTimeFormatter;
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
        DateTimeFormatter timeFormatter =
                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
        MockitoAnnotations.initMocks(this);
        Task task =
                new PrintBooksByFilterTask("print books by filter",
                        this.searcher,
                        this.filter,
                        timeFormatter);

        // Act
        task.run();

        // Assert
        verify(searcher).search(filter);
    }
}