package org.plasma.digitalib.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class BookIdentifier implements Serializable {
    private final String name;
    private final String author;
}
