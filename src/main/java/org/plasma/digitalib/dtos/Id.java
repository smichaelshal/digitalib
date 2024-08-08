package org.plasma.digitalib.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Id {
    protected final UUID id;
}
