package org.plasma.digitalib.tasks;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddBookCopyTaskTest {
    @Mock
    ItemAdder<Book> storageBookAdder;

    @Mock
    Input consoleInput;

    @Test
    void run() {
        // Arrange
        MockitoAnnotations.initMocks(this);
        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(consoleInput);
        Task task = new AddBookCopyTask("add new book", storageBookAdder,
                creatorBookIdentifier, consoleInput);
        when(consoleInput.getNotEmptyParameter(any()))
                .thenReturn("book");
        when(consoleInput.getParameter(any()))
                .thenReturn("book");
        when(storageBookAdder.add(any(Book.class)))
                .thenReturn(true);
        // Act
        task.run();

        // Assert
        verify(storageBookAdder).add(any());

    }
}