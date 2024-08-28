package org.plasma.digitalib.configs;

import org.plasma.digitalib.filters.AllBookFilter;
import org.plasma.digitalib.filters.BorrowedBookFilter;
import org.plasma.digitalib.filters.ExpiredBookFilter;
import org.plasma.digitalib.filters.PresentBookFilter;
import org.plasma.digitalib.searchers.BookSearcher;
import org.plasma.digitalib.tasks.PrintBooksByFilterTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = AllBookFilter.class)
public class FilterConfig {

    @Bean
    public static AllBookFilter allBookFilter() {
        return new AllBookFilter();
    }

    @Bean
    public static BorrowedBookFilter borrowedBookFilter() {
        return new BorrowedBookFilter();
    }

    @Bean
    public static ExpiredBookFilter expiredBookFilter() {
        return new ExpiredBookFilter();
    }

    @Bean
    public static PresentBookFilter presentBookFilter() {
        return new PresentBookFilter();
    }

    @Bean
    public static PrintBooksByFilterTask printBooksByFilterExpiredTask(
            final BookSearcher searcher,
            final ExpiredBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print expired books",
                searcher,
                filter);
    }

    @Bean
    public static PrintBooksByFilterTask printBooksByFilterBorrowedTask(
            final BookSearcher searcher,
            final BorrowedBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print borrowed books",
                searcher,
                filter);
    }

    @Bean
    public static PrintBooksByFilterTask printAllBooksTask(
            final BookSearcher searcher,
            final AllBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print all books",
                searcher,
                filter);
    }

    @Bean
    public static PrintBooksByFilterTask printBooksByFilterPresentTask(
            final BookSearcher searcher,
            final PresentBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print present books",
                searcher,
                filter);
    }
}

