package org.plasma.digitalib.Adder;

import lombok.AllArgsConstructor;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.storage.Storage;

import java.util.function.Function;

@AllArgsConstructor
public class StorageBookAdder implements ItemAdder<Book> {
    private final Storage<Book> storage;
    private final Function<Book, Boolean> bookIdentifierFilter;

    public final boolean add(Book book) {

        return this.storage.create(book);
    }
}
