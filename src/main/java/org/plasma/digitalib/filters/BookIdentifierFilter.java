package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import org.plasma.digitalib.dtos.Book;
import org.plasma.digitalib.dtos.BookIdentifier;

import java.util.function.Function;

@AllArgsConstructor
public class BookIdentifierFilter implements Function<Book, Boolean> {
    private final BookIdentifier expectedBookIdentifier;

    public final Boolean apply(final Book book) {
        BookIdentifier bookIdentifier = book.getBookIdentifier();
        return bookIdentifier.getName()
                .equals(this.expectedBookIdentifier.getName())
                && bookIdentifier.getAuthor()
                .equals(this.expectedBookIdentifier.getAuthor());
    }
}
