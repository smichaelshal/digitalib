package org.plasma.digitalib.filters;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.function.Function;

@RequiredArgsConstructor
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
