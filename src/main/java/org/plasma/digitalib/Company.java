package org.plasma.digitalib;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class Company {
    private Address address;

    public Company(Address address) {
        this.address = address;
    }

    // getter, setter and other properties
}

