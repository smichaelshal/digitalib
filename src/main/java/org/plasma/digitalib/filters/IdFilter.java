package org.plasma.digitalib.filters;

import org.plasma.digitalib.dtos.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public class IdFilter<T extends BorrowableItem> implements Function<T, Boolean> {
    private final List<UUID> ids;

    public IdFilter(List<UUID> ids) {
        this.ids = ids;
    }

    public Boolean apply(T item) {
        return ids.contains(item.getId());
    }
}
