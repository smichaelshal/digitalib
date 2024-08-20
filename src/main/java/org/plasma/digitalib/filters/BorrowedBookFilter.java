package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.Book;

import java.util.function.Predicate;

@Slf4j
@AllArgsConstructor
public class BorrowedBookFilter implements Predicate<Book> {
    public final boolean test(final Book book) {
        boolean isBorrowed = book.getIsBorrowed();
        log.debug("{} is {}borrowed", book, (isBorrowed ? "" : "not "));
        return isBorrowed;
    }
}
