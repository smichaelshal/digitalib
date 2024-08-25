package org.plasma.digitalib.tasks;

import org.plasma.digitalib.adders.ItemAdder;
import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.models.Book;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = AddNewBookTask.class)
public class TaskConfig {
    @Bean
    public AddNewBookTask addNewBookTask(
            final ItemAdder<Book> adder,
            final ConsoleCreatorBookIdentifier creatorBookIdentifier,
            final Input consoleUtils) {
        return new AddNewBookTask(
                "add new book",
                adder,
                creatorBookIdentifier,
                consoleUtils);
    }

    @Bean
    public ConsoleCreatorBookIdentifier consoleCreatorBookIdentifier(
            final Input consoleInput
    ) {
        return new ConsoleCreatorBookIdentifier(consoleInput);
    }
}

