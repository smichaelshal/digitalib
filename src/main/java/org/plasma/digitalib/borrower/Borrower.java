package org.plasma.digitalib.borrower;

import org.plasma.digitalib.models.BorrowingResult;
import org.plasma.digitalib.models.OrderRequest;

public interface Borrower<T> {
    BorrowingResult borrowItem(OrderRequest<T> request);
    boolean returnItem(OrderRequest<T> request);
}
