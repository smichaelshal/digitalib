package org.plasma.digitalib.filters;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.plasma.digitalib.models.BorrowableItem;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Slf4j
@AllArgsConstructor
public class IdsFilter<T extends BorrowableItem>
        implements Function<T, Boolean> {
    private final List<UUID> ids;

    public final Boolean apply(final T item) {
        log.info("{}, id: {} borrowed", item.getId().toString());
        return ids.contains(item.getId());
    }
}