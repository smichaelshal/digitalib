package org.plasma.digitalib.filters;

import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.Book;

import java.util.function.Predicate;

@Slf4j
public class PresentBookFilter implements Predicate<Book> {
    public final boolean test(final Book book) {
        boolean isPresent = !book.getIsBorrowed();
        log.debug("{} is {}present", book, (isPresent ? "" : "not "));
        return isPresent;
    }
}
