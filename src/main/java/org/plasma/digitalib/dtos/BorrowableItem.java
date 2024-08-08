package org.plasma.digitalib.dtos;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class BorrowableItem implements Serializable {
    protected final List<Borrowing> borrowings;
    protected final Instant enteredTime;
    protected final boolean isBorrowed;
    protected final UUID Id;
}
