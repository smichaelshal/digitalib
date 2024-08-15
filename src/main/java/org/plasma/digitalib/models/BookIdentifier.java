package org.plasma.digitalib.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookIdentifier implements Serializable {
    private String name;
    private String author;
}
