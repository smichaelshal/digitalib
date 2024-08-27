package org.plasma.digitalib.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FilePersistenterStorageTest {
    private List<Book> listStorage;
    private List<Book> books;
    private FilePersistenterStorage<Book> storage;
    private Book book;

    @Mock
    private Predicate<Book> bookByIdFilter;

    @BeforeEach
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
        this.books = new LinkedList<>();
        this.listStorage = new LinkedList<>();
        Path path = Files.createTempDirectory(UUID.randomUUID().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        this.storage = new FilePersistenterStorage<>(
                this.listStorage,
                path.toString(),
                objectMapper,
                new TypeReference<>() { });
        this.book = new Book(
                "genre",
                "summary",
                new BookIdentifier("name", "author"));
    }

    @Test
    public void add_withBook_shouldAddToList() {
        // Arrange
        this.books.add(this.book);

        // Act
        this.storage.create(this.book);

        // Assert
        assertEquals(this.books, this.listStorage);
    }

    @Test
    public void add_withBook_shouldReturnTrue() {
        // Arrange
        this.books.add(this.book);

        // Act
        boolean addResult = this.storage.create(this.book);

        // Assert
        assertTrue(addResult);
    }

    @Test
    public void read_withIdFilter_shouldReturnBook() {
        // Arrange
        when(bookByIdFilter.test(this.book)).thenReturn(true);

        this.storage.create(this.book);

        // Act
        List<Book> bookResults = this.storage.readAll(bookByIdFilter);

        // Assert
        assertEquals(List.of(this.book), bookResults);
    }

    @Test
    public void read_withIdFilter_shouldNotReturnBook() {
        // Arrange
        when(bookByIdFilter.test(any())).thenReturn(false);

        this.storage.create(this.book);

        // Act
        List<Book> bookResults = this.storage.readAll(bookByIdFilter);

        // Assert
        assertEquals(List.of(), bookResults);
    }

    @Test
    public void update_withNewBorrowing_shouldUpdatedBookInList() {
        // Arrange
        this.storage.create(this.book);
        Book bookCopy = SerializationUtils.clone(this.book);
        Borrowing borrowing = new Borrowing(
                new User("5678"),
                Instant.now(),
                Instant.now().plus(1000, ChronoUnit.SECONDS));
        bookCopy.getBorrowings().add(borrowing);
        this.books.add(bookCopy);

        // Act
        this.storage.update(this.book.getId(), bookCopy);

        // Assert
        assertEquals(this.books, this.listStorage);
    }

    @Test
    public void update_withNewBorrowing_shouldReturnTrue() {
        // Arrange
        this.storage.create(this.book);
        Book bookCopy = SerializationUtils.clone(this.book);
        Borrowing borrowing = new Borrowing(
                new User("5678"),
                Instant.now(),
                Instant.now().plus(1000, ChronoUnit.SECONDS));
        bookCopy.getBorrowings().add(borrowing);
        this.books.add(bookCopy);

        // Act
        boolean updateResult = this.storage.update(this.book.getId(), bookCopy);

        // Assert
        assertTrue(updateResult);
    }
}