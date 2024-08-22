package org.plasma.digitalib.borrower;

import lombok.RequiredArgsConstructor;
import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.User;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
public class BaseBorrowingFactory implements BorrowingFactory {
    private final Duration durationBorrowing;

    public final Borrowing create(final User user) {
        Instant currentTime = Instant.now();
        return new Borrowing(
                user,
                currentTime,
                Optional.empty(),
                currentTime.plus(durationBorrowing));
    }
}
