package org.plasma.digitalib.storage;

import lombok.AllArgsConstructor;
import org.plasma.digitalib.models.Book;

import java.util.UUID;
import java.util.function.Function;

@AllArgsConstructor
public class BookFilterById implements Function<Book, Boolean> {
    private final UUID id;

    public Boolean apply(Book book) {
        return this.id.equals(book.getId());
    }
}
