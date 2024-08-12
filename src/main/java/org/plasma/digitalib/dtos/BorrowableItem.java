package org.plasma.digitalib.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class BorrowableItem implements Serializable {
    protected List<Borrowing> borrowings;
    protected Instant enteredTime;
    protected boolean isBorrowed;
    protected UUID Id;

    public BorrowableItem(List<Borrowing> borrowings, Instant enteredTime, boolean isBorrowed, UUID id) {
        this.borrowings = borrowings;
        this.enteredTime = enteredTime;
        this.isBorrowed = isBorrowed;
        Id = id;
    }
}
