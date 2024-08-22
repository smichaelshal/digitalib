package org.plasma.digitalib;

import org.plasma.digitalib.adders.StorageBookAdder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        StorageBookAdder adder =
                (StorageBookAdder) context.getBean(
                        "storageBookAdder");
        System.out.println(adder);
    }
}
