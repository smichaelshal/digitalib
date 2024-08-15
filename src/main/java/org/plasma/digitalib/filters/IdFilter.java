package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import org.plasma.digitalib.models.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@AllArgsConstructor
public class IdFilter<T extends BorrowableItem>
        implements Function<T, Boolean> {
    private final List<UUID> ids;

    public final Boolean apply(final T item) {
        return ids.contains(item.getId());
    }
}
