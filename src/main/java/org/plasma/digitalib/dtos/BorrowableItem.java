package org.plasma.digitalib.dtos;

import lombok.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class BorrowableItem implements Serializable {
    protected List<Borrowing> borrowings;
    protected Instant enteredTime;
    protected Boolean isBorrowed;
    protected UUID id;

    public BorrowableItem(List<Borrowing> borrowings, Instant enteredTime, boolean isBorrowed, UUID id) {
        this.borrowings = borrowings;
        this.enteredTime = enteredTime;
        this.isBorrowed = isBorrowed;
        this.id = id;
    }
}
