package org.plasma.digitalib.borrower;

import org.plasma.digitalib.models.Borrowing;
import org.plasma.digitalib.models.User;

public interface BorrowingFactory {
    Borrowing create(User user);
}
