package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
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
