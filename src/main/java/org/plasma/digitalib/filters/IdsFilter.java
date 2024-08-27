package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

@Slf4j
@AllArgsConstructor
public class IdsFilter<T extends BorrowableItem> implements Predicate<T> {
    private final List<UUID> ids;

    public final boolean test(final T item) {
        boolean isContains = ids.contains(item.getId());
        log.debug("The id of {} is {}contains in {}",
                item,
                this.ids,
                isContains ? "" : "not ");
        return isContains;
    }
}
