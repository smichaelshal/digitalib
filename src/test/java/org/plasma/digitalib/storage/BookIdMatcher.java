package org.plasma.digitalib.storage;

import lombok.AllArgsConstructor;
import org.mockito.ArgumentMatcher;
import org.plasma.digitalib.models.Book;

import java.util.UUID;

@AllArgsConstructor
public class BookIdMatcher implements ArgumentMatcher<Book> {
    private final UUID id;
    @Override
    public boolean matches(Book book) {
        return book.getId().equals(this.id);
    }
}
