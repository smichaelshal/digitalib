package org.plasma.digitalib;

import org.plasma.digitalib.searchers.BookSearcher;
import org.plasma.digitalib.tasks.AddNewBookTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Application {
    public static void main(final String[] args) {
        ConfigurableApplicationContext
                context = SpringApplication.run(Application.class, args);
        AddNewBookTask addNewBookTask =
                (AddNewBookTask) context.getBean(
                        "addNewBookTask");
        System.out.println(addNewBookTask);
    }
}
