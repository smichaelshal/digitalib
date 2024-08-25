package org.plasma.digitalib.filters;

import org.plasma.digitalib.searchers.BookSearcher;
import org.plasma.digitalib.tasks.PrintBooksByFilterTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = AllBookFilter.class)
public class FilterConfig {
    @Bean
    public AllBookFilter allBookFilter() {
        return new AllBookFilter();
    }

    @Bean
    public BorrowedBookFilter borrowedBookFilter() {
        return new BorrowedBookFilter();
    }

    @Bean
    public ExpiredBookFilter expiredBookFilter() {
        return new ExpiredBookFilter();
    }

    @Bean
    public PresentBookFilter presentBookFilter() {
        return new PresentBookFilter();
    }

    @Bean
    public PrintBooksByFilterTask printBooksByFilterExpiredTask(
            final BookSearcher searcher,
            final ExpiredBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print expired books",
                searcher,
                filter);
    }

    @Bean
    public PrintBooksByFilterTask printBooksByFilterBorrowedTask(
            final BookSearcher searcher,
            final BorrowedBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print borrowed books",
                searcher,
                filter);
    }

    @Bean
    public PrintBooksByFilterTask printAllBooksTask(
            final BookSearcher searcher,
            final AllBookFilter filter) {
        return new PrintBooksByFilterTask(
                "print all books",
                searcher,
                filter);
    }
}

