package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BorrowableItem implements Serializable {
    protected List<Borrowing> borrowings;
    protected Instant enteredTime;
    protected Boolean isBorrowed;
    protected UUID id;
}
