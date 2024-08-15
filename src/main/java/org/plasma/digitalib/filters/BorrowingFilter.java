package org.plasma.digitalib.filters;

import org.plasma.digitalib.models.BorrowableItem;

import java.util.function.Function;

public final class BorrowingFilter<T extends BorrowableItem>
        implements Function<T, Boolean> {
    public Boolean apply(final T item) {
        return item.getIsBorrowed();
    }
}
