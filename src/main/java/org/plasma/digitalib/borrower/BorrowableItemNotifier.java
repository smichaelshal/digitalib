package org.plasma.digitalib.borrower;

import org.plasma.digitalib.models.BorrowableItem;

public interface BorrowableItemNotifier<T extends BorrowableItem> {
    boolean add(T item);
    boolean delete(T item);
}
