package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Borrowing implements Serializable {
    private User user;
    private Instant borrowingTime;
    private Optional<Instant> returnTime;
    private Instant expiredTime;

    public Borrowing(final User userBorrowed,
                     final Instant startBorrowingTime,
                     final Instant expiredTimeBorrowing) {
        this.user = userBorrowed;
        this.borrowingTime = startBorrowingTime;
        this.returnTime = Optional.ofNullable(null);
        this.expiredTime = expiredTimeBorrowing;
    }
}
