package org.plasma.digitalib.filters;

import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.BorrowableItem;

import java.util.function.Predicate;

@Slf4j
public final class BorrowingFilter<T extends BorrowableItem>
        implements Predicate<T> {
    public boolean test(final T item) {
        log.debug("{} is{} borrowed",
                item,
                ((item.getIsBorrowed()) ? "" : " not"));
        return item.getIsBorrowed();
    }
}
