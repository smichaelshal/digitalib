package org.plasma.digitalib.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Id implements Serializable {
    protected UUID id;
}
