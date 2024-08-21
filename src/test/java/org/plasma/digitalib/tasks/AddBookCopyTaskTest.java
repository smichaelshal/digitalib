package org.plasma.digitalib.tasks;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddBookCopyTaskTest {
    @InjectMocks
    Task task;

    @Mock
    ItemAdder<Book> storageBookAdder;

    @Mock
    Input consoleInput;

    @Captor
    ArgumentCaptor<Book> bookCaptor;

    @Test
    void run() {
        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(consoleInput);

        this.task = new AddNewBookTask("add new book", storageBookAdder,
                creatorBookIdentifier, consoleInput);

        Book expectedBook = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        when(consoleInput.getNotEmptyParameter("summary book"))
                .thenReturn(expectedBook.getSummary());
        when(consoleInput.getNotEmptyParameter("genre book"))
                .thenReturn(expectedBook.getGenre());
        when(consoleInput.getNotEmptyParameter("book name"))
                .thenReturn(expectedBook.getBookIdentifier().getName());
        when(consoleInput.getNotEmptyParameter("book author"))
                .thenReturn(expectedBook.getBookIdentifier().getAuthor());

        verify(storageBookAdder).add(bookCaptor.capture());

        // Act
        this.task.run();

        // Assert
        assertTrue(this.isValidBook(bookCaptor.getValue(), expectedBook));
    }

    private boolean isValidBook(
            final Book book,
            final Book expectedBook) {

        return book.getSummary().equals(expectedBook.getSummary())
                && book.getGenre().equals(expectedBook.getGenre())
                && book.getBookIdentifier()
                .equals(expectedBook.getBookIdentifier());
    }
}