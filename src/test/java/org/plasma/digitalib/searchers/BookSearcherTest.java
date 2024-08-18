package org.plasma.digitalib.searchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.BookIdMatcher;
import org.plasma.digitalib.storage.Storage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BookSearcherTest {
    private Storage<Book> storage;
    private Function<Book, Boolean> filter;
    private BookSearcher searcher;

    @BeforeEach
    void setup() {
        this.storage = mock(Storage.class);
        this.filter = mock(Function.class);
        this.searcher = new BookSearcher(this.storage);
    }

    @Test
    void search() {
        // Arrange
        List<Book> expectedResult = new LinkedList();
        when(this.storage.readAll(any(Function.class))).thenReturn(expectedResult);

        // Act
        List<Book> searchResult = this.searcher.search(this.filter);

        // Assert
        assertEquals(expectedResult, searchResult);
    }
}
