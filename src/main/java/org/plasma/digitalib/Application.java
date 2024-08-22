package org.plasma.digitalib;

import org.plasma.digitalib.storage.FilePersistenterStorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        FilePersistenterStorage storage = (FilePersistenterStorage) context
                .getBean("bookFilePersistenterStorage");
        System.out.println(storage);
    }
}
