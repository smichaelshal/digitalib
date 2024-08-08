package org.plasma.digitalib.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BookIdentifier {
    private final String name;
    private final String author;
}
