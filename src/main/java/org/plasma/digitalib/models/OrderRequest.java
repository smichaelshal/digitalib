package org.plasma.digitalib.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrderRequest<T> {
    private final User user;
    private final T itemIdentifier;
}
