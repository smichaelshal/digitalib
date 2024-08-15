package org.plasma.digitalib.adders;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;

import java.util.List;
import java.util.function.Function;

@AllArgsConstructor
public class StorageBookAdder implements ItemAdder<Book> {
    private final Storage<Book> storage;
    private final Function<Book, Boolean> bookIdentifierFilter;

    public final boolean add(@NonNull final Book book) {
        List<Book> bookResults = this.storage
                .readAll(this.bookIdentifierFilter);
        for (Book bookResult : bookResults) {
            if (!bookResult.getSummary().equals(book.getSummary())
                    || !bookResult.getGenre().equals(book.getGenre())) {
                return false;
            }
        }
        return this.storage.create(book);
    }
}
