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
    protected final Boolean isBorrowed;
    protected final UUID Id;

    protected BorrowableItem(List<Borrowing> borrowings, Instant enteredTime, boolean isBorrowed, UUID id) {
        this.borrowings = borrowings;
        this.enteredTime = enteredTime;
        this.isBorrowed = isBorrowed;
        Id = id;
    }
}
