package org.plasma.digitalib.filters;

import org.plasma.digitalib.models.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class IdFilter<T extends BorrowableItem>
        implements Function<T, Boolean> {
    private final List<UUID> ids;

    public IdFilter(final List<UUID> uuids) {
        this.ids = uuids;
    }

    public final Boolean apply(final T item) {
        return ids.contains(item.getId());
    }
}
