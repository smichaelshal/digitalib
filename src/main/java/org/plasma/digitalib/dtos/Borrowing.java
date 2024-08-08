package org.plasma.digitalib.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public class Borrowing implements Serializable {
    private final User user;
    private final Instant borrowingTime;
    private final Optional<Instant> returnTime;
    private final Instant expiredTime;
}
