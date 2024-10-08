package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Borrowing implements Serializable {
    private User user;
    private Instant borrowingTime;
    private Optional<Instant> returnTime;
    private Instant expiredTime;

    public Borrowing(final User user,
                     final Instant borrowingTime,
                     final Instant expiredTime) {
        this.user = user;
        this.borrowingTime = borrowingTime;
        this.returnTime = Optional.empty();
        this.expiredTime = expiredTime;
    }
}
