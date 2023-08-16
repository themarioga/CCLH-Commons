package org.themarioga.cclh.commons;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.themarioga.cclh.commons.configurations.ApplicationConfig;
import org.themarioga.cclh.commons.configurations.DataAccessConfig;
import org.themarioga.cclh.commons.configurations.FlywayConfig;

@Configuration
@Import({ApplicationConfig.class, DataAccessConfig.class, FlywayConfig.class})
@ComponentScan("org.themarioga.cclh")
public class TestConfiguration {
}
