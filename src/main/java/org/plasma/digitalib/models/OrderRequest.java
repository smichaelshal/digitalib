package org.plasma.digitalib.models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// T extends BorrowableItem ???
@Getter
@RequiredArgsConstructor
public class OrderRequest<T> {
    private final User user;
    private final T itemIdentifier;
}
