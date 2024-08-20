package org.plasma.digitalib.borrower;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.SerializationUtils;
import org.mockito.ArgumentMatcher;
import org.plasma.digitalib.models.Book;
import org.plasma.digitalib.models.Borrowing;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
public class BookBorrowingMatcher implements ArgumentMatcher<Book> {
    private final Book expectedBook;
    private final Duration borrowingDuration;


    @Override
    public boolean matches(Book book) {
        Book copyBook = SerializationUtils.clone(book);
        List<Borrowing> borrowings = copyBook.getBorrowings();
        if (borrowings.size() == this.expectedBook.getBorrowings().size()) {
            return false;
        }

        Borrowing lastBorrowing = borrowings.get(borrowings.size() - 1);
        Instant borrowingTime = lastBorrowing.getBorrowingTime();
        if (!this.isInRange(borrowingTime)) {
            return false;
        }

        if(!borrowingTime.equals(borrowingTime.plus(this.borrowingDuration))){
            return false;
        }

        lastBorrowing.setBorrowingTime(null);
        lastBorrowing.setExpiredTime(null);

        List<Borrowing> expectedLastBorrowings = this.expectedBook
                .getBorrowings();
        Borrowing expectedLastBorrowing = expectedLastBorrowings
                .get(expectedLastBorrowings.size() - 1);

        expectedLastBorrowing.setExpiredTime(null);
        expectedLastBorrowing.setExpiredTime(null);

        return this.expectedBook.equals(copyBook);
    }

    private boolean isInRange(Instant borrowingTime) {
        Instant currentTime = Instant.now();
        return borrowingTime.isAfter(currentTime
                .plus(30, ChronoUnit.SECONDS))
                || borrowingTime.isBefore(currentTime
                .minus(30, ChronoUnit.SECONDS));
    }
}