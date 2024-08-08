package org.plasma.digitalib.dtos;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class User implements Serializable {
    private final String id;
}
