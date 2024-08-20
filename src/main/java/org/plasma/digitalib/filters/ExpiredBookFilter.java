package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.Borrowing;

import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
@AllArgsConstructor
public class ExpiredBookFilter implements Predicate<Book> {
    public final boolean test(final Book book) {
        if (book.getIsBorrowed()) {
            return false;
        }

        List<Borrowing> borrowings = book.getBorrowings();
        if (borrowings.isEmpty()) {
            log.debug("0 borrowings of {}", book);
            return false;
        }
        Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
        return Instant.now().isAfter(lastBorrowing.getExpiredTime());
    }
}
