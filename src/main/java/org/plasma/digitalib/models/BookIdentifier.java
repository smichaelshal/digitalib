package org.plasma.digitalib.models;

import lombok.*;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class BookIdentifier implements Serializable {
    private String name;
    private String author;
}
