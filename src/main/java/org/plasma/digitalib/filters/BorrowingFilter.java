package org.plasma.digitalib.filters;

import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.BorrowableItem;

import java.util.function.Function;

@Slf4j
public final class BorrowingFilter<T extends BorrowableItem>
        implements Function<T, Boolean> {
    public Boolean apply(final T item) {
        log.info("{} is{} borrowed",
                item,
                ((item.getIsBorrowed()) ? "" : " not"));
        return item.getIsBorrowed();
    }
}
