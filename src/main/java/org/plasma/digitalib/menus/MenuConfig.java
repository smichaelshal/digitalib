package org.plasma.digitalib.menus;

import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.tasks.AddBookCopyTask;
import org.plasma.digitalib.tasks.AddNewBookTask;
import org.plasma.digitalib.tasks.BorrowBookTask;
import org.plasma.digitalib.tasks.PrintBooksByFilterTask;
import org.plasma.digitalib.tasks.ReturnBookTask;
import org.plasma.digitalib.tasks.Task;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = HeaderDisplayer.class)
public class MenuConfig {
    @Bean
    public HeaderDisplayer headerDisplayerMenu() {
        return new HeaderDisplayer("menu");
    }
    @Bean
    public ForeverMenu foreverMenu(MapMenu menu){
        return new ForeverMenu(menu);
    }


    @Bean
    public MapMenu mapMenu(
            final HeaderDisplayer displayer,
            final Input input,
            AddNewBookTask addNewBookTask,
            AddBookCopyTask addBookCopyTask,
            BorrowBookTask borrowBookTask,
            ReturnBookTask returnBookTask,
            PrintBooksByFilterTask printBooksByFilterExpiredTask,
            PrintBooksByFilterTask printBooksByFilterBorrowedTask,
            PrintBooksByFilterTask printBooksByFilterPresentTask,
            PrintBooksByFilterTask printAllBooksTask) {
        Map<String, Task> tasks = new HashMap<>();

        tasks.put("1", addNewBookTask);
        tasks.put("2", addBookCopyTask);
        tasks.put("3", borrowBookTask);
        tasks.put("4", returnBookTask);

        tasks.put("5", printBooksByFilterExpiredTask);
        tasks.put("6", printBooksByFilterBorrowedTask);
        tasks.put("7", printBooksByFilterPresentTask);
        tasks.put("8", printAllBooksTask);


        return new MapMenu(displayer, tasks, input);
    }
}




