package org.plasma.digitalib.borrower;

import org.plasma.digitalib.dtos.BorrowableItem;

public interface BorrowableItemNotifier<T extends BorrowableItem> {
    boolean add(T item);
    boolean delete(T item);
}
