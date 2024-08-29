package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.BookIdentifier;

import java.util.function.Predicate;

@Slf4j
@AllArgsConstructor
public class BookIdentifierFilter implements Predicate<Book> {
    private final BookIdentifier expectedBookIdentifier;

    public final boolean test(final Book book) {
        BookIdentifier bookIdentifier = book.getBookIdentifier();
        boolean filterResult =
                bookIdentifier.equals(this.expectedBookIdentifier);

        log.debug("The book are {}match to filter"
                        + " by target bookIdentifier: {}: {}",
                filterResult ? "not " : "", this.expectedBookIdentifier, book);

        return filterResult;
    }
}