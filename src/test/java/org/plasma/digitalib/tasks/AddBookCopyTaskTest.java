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
    private ItemAdder<Book> storageBookAdder;

    @Mock
    private Input consoleInput;

    @Test
    void run_withStringInput_shouldCallAdd() {
        // Arrange
        MockitoAnnotations.initMocks(this);
        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(consoleInput);
        Task task = new AddBookCopyTask(
                "add copy book",
                storageBookAdder,
                creatorBookIdentifier);
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