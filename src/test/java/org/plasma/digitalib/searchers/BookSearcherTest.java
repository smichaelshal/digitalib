package org.plasma.digitalib.searchers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookSearcherTest {
    private BookSearcher searcher;

    @Mock
    private Storage<Book> storage;

    @Mock
    private Predicate<Book> filter;

    @BeforeEach
    void setup() {
        MockitoAnnotations.initMocks(this);
        this.searcher = new BookSearcher(this.storage);
    }

    @Test
    void search() {
        // Arrange
        List<Book> expectedResult = new LinkedList<>();
        when(this.storage.readAll(this.filter)).thenReturn(expectedResult);

        // Act
        List<Book> searchResult = this.searcher.search(this.filter);

        // Assert
        assertEquals(expectedResult, searchResult);
    }
}
