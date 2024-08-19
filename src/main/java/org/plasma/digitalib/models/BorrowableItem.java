package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class BorrowableItem implements Serializable {
    protected List<Borrowing> borrowings;
    protected Instant enteredTime;
    protected Boolean isBorrowed;
    protected UUID id;
}
