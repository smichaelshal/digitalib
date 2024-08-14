package org.plasma.digitalib.Adder;

import lombok.AllArgsConstructor;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.storage.Storage;

@AllArgsConstructor
public class BookAdder implements ItemAdder<Book> {
    private final Storage<Book> storage;

    public final boolean add(Book book) {
        return false;
    }
}
