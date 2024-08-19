package org.plasma.digitalib.filters;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.function.Predicate;

@RequiredArgsConstructor
public class BookIdentifierFilter implements Predicate<Book> {
    private final BookIdentifier expectedBookIdentifier;

    public final boolean test(final Book book) {
        BookIdentifier bookIdentifier = book.getBookIdentifier();
        return bookIdentifier.getName()
                .equals(this.expectedBookIdentifier.getName())
                && bookIdentifier.getAuthor()
                .equals(this.expectedBookIdentifier.getAuthor());
    }
}
