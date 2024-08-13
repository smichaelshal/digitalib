package org.plasma.digitalib.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public BorrowableItem(final List<Borrowing> borrowings,
                          final Instant enteredTime,
                          final Boolean isBorrowed,
                          final UUID id) {
        this.borrowings = borrowings;
        this.enteredTime = enteredTime;
        this.isBorrowed = isBorrowed;
        this.id = id;
    }
}
