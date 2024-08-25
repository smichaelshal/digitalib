package org.plasma.digitalib.filters;

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
}

