package org.plasma.digitalib.adders;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.filters.BookIdentifierFilter;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.storage.Storage;

import java.util.List;

@Slf4j
@AllArgsConstructor
public class StorageBookAdder implements ItemAdder<Book> {
    private final Storage<Book> storage;

    public final boolean add(final Book book) {
        List<Book> bookResults = this.storage
                .readAll(
                        new BookIdentifierFilter(book.getBookIdentifier()));
        for (Book bookResult : bookResults) {
            if (!bookResult.getSummary().equals(book.getSummary())) {
                if (book.getSummary().isEmpty()) {
                    book.setSummary(bookResult.getSummary());
                } else {
                    return false;
                }
            }

            if (!bookResult.getGenre().equals(book.getGenre())) {
                if (book.getGenre().isEmpty()) {
                    book.setGenre(bookResult.getGenre());
                } else {
                    return false;
                }
            }
        }

        if (book.getBookIdentifier().getName().isEmpty()
                || book.getBookIdentifier().getAuthor().isEmpty()
                || book.getSummary().isEmpty()
                || book.getGenre().isEmpty()) {
            return false;
        }

        log.debug("try add {} to storage", book);
        return this.storage.create(book);
    }
}
