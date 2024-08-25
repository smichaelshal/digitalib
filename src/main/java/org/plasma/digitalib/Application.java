package org.plasma.digitalib;

import org.plasma.digitalib.tasks.AddBookCopyTask;
import org.plasma.digitalib.tasks.BorrowBookTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        BorrowBookTask borrowBookTask =
                (BorrowBookTask) context.getBean(
                        "borrowBookTask");
        System.out.println(borrowBookTask);
    }
}
