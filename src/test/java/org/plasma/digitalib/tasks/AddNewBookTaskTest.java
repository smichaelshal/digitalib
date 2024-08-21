package org.plasma.digitalib.tasks;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.plasma.digitalib.adders.StorageBookAdder;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@RunWith(MockitoJUnitRunner.class)
class AddNewBookTaskTest {

    private Task task;
    private Input consoleInput;
    private StorageBookAdder storageBookAdder;


//    @Mock
//    StorageBookAdder storageBookAdder;

//    @Mock
//    Input consoleInput;


//    @BeforeEach
    public void setup() {
//        MockitoAnnotations.initMocks(this);

    }

//    @Captor
//    ArgumentCaptor<Book> bookCaptor;

    private boolean isValidBook(
            final Book book,
            final Book expectedBook) {

        return book.getSummary().equals(expectedBook.getSummary())
                && book.getGenre().equals(expectedBook.getGenre())
                && book.getBookIdentifier()
                .equals(expectedBook.getBookIdentifier());
    }

    @Test
    public void test() {

        this.consoleInput = mock(Input.class);
        this.storageBookAdder = mock(StorageBookAdder.class);

        ConsoleCreatorBookIdentifier creatorBookIdentifier =
                new ConsoleCreatorBookIdentifier(this.consoleInput);

        this.task = new AddNewBookTask("add new book", this.storageBookAdder,
                creatorBookIdentifier, this.consoleInput);

        // Arrange
//        this.consoleInput = mock(Input.class);
        ArgumentCaptor bookCaptor = ArgumentCaptor.forClass(Book.class);

        Book expectedBook = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));

        when(this.consoleInput.getNotEmptyParameter("summary book"))
                .thenReturn(expectedBook.getSummary());
        when(this.consoleInput.getNotEmptyParameter("genre book"))
                .thenReturn(expectedBook.getGenre());
        when(this.consoleInput.getNotEmptyParameter("book name"))
                .thenReturn(expectedBook.getBookIdentifier().getName());
        when(this.consoleInput.getNotEmptyParameter("book author"))
                .thenReturn(expectedBook.getBookIdentifier().getAuthor());

        verify(this.storageBookAdder).add((Book) bookCaptor.capture());

        // Act
//        this.task.run();

        // Assert
        assertTrue(this.isValidBook((Book) bookCaptor.getValue(), expectedBook));
    }
}