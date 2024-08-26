package org.plasma.digitalib.menus;

import org.plasma.digitalib.inputs.Input;
import org.plasma.digitalib.tasks.AddBookCopyTask;
import org.plasma.digitalib.tasks.AddNewBookTask;
import org.plasma.digitalib.tasks.BorrowBookTask;
import org.plasma.digitalib.tasks.ExitTask;
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
    /**
     * @return
     */
    @Bean
    public HeaderDisplayer headerDisplayerMenu() {
        return new HeaderDisplayer("Enter the number of the option"
                + " you want to perform: ");
    }

    /**
     * @param menu
     * @return
     */
    @Bean
    public ForeverMenu foreverMenu(final MapMenu menu) {
        return new ForeverMenu(menu);
    }

    /**
     * @param displayer
     * @param input
     * @param addNewBookTask
     * @param addBookCopyTask
     * @param borrowBookTask
     * @param returnBookTask
     * @param printBooksByFilterExpiredTask
     * @param printBooksByFilterBorrowedTask
     * @param printBooksByFilterPresentTask
     * @param printAllBooksTask
     * @param exitTask
     * @return
     */
    @Bean
    public MapMenu mapMenu(
            final HeaderDisplayer displayer,
            final Input input,
            final AddNewBookTask addNewBookTask,
            final AddBookCopyTask addBookCopyTask,
            final BorrowBookTask borrowBookTask,
            final ReturnBookTask returnBookTask,
            final PrintBooksByFilterTask printBooksByFilterExpiredTask,
            final PrintBooksByFilterTask printBooksByFilterBorrowedTask,
            final PrintBooksByFilterTask printBooksByFilterPresentTask,
            final PrintBooksByFilterTask printAllBooksTask,
            final ExitTask exitTask) {
        Map<String, Task> tasks = new HashMap<>();

        tasks.put("1", addNewBookTask);
        tasks.put("2", addBookCopyTask);
        tasks.put("3", borrowBookTask);
        tasks.put("4", returnBookTask);

        tasks.put("5", printBooksByFilterExpiredTask);
        tasks.put("6", printBooksByFilterBorrowedTask);
        tasks.put("7", printBooksByFilterPresentTask);
        tasks.put("8", printAllBooksTask);

        tasks.put("9", exitTask);

        return new MapMenu(displayer, tasks, input);
    }
}




