package com.coolmind.ordertracker.web.config.root;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;

/**
 *
 * The root context configuration of the application - the beans in this context will be globally visible
 * in all servlet contexts.
 *
 */

@Configuration
@ComponentScan(
        includeFilters = {
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*services\\.[^.]*"),
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*dao\\.[^.]*"),
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*init\\.[^.]*"),
            @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*security\\.[^.]*")
        }, basePackages = {"com.coolmind.ordertracker"}
)
public class RootContextConfig {

    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory,
                                                         DriverManagerDataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }

}
