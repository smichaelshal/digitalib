package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@Setter
@Getter
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
        this.returnTime = Optional.ofNullable(null);
        this.expiredTime = expiredTime;
    }
}
