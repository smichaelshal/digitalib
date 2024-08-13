package org.plasma.digitalib.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Borrowing {
    private User user;
    private Instant borrowingTime;
    private Optional<Instant> returnTime;
    private Instant expiredTime;

    public Borrowing(final User user,
                     final Instant borrowingTime,
                     final Instant expiredTime) {
        this.user = user;
        this.borrowingTime = borrowingTime;
        this.returnTime = Optional.ofNullable(null);
        this.expiredTime = expiredTime;
    }
}
