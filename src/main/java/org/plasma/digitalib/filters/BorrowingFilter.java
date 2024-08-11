package org.plasma.digitalib.filters;

import org.plasma.digitalib.dtos.BorrowableItem;

import java.util.function.Function;

public class BorrowingFilter<T extends BorrowableItem> implements Function<T, Boolean> {
    public Boolean apply(T item) {
        return item.getIsBorrowed();
    }
}
