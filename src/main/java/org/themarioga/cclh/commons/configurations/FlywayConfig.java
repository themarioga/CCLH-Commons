package org.themarioga.cclh.commons.configurations;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.sql.DataSource;

@Configuration
public class FlywayConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    @DependsOn("dataSource")
    public Flyway flyway() {
        Flyway flyway = Flyway.configure().dataSource(dataSource).locations("classpath:db/migrations/").baselineOnMigrate(true).validateOnMigrate(false).load();
        flyway.migrate();
        return flyway;
    }

}
