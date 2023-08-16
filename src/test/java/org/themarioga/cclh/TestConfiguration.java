package org.themarioga.cclh;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.themarioga.cclh.configurations.ApplicationConfig;
import org.themarioga.cclh.configurations.DataAccessConfig;
import org.themarioga.cclh.configurations.FlywayConfig;

@Configuration
@Import({ApplicationConfig.class, DataAccessConfig.class, FlywayConfig.class})
@ComponentScan("org.themarioga.cclh")
public class TestConfiguration {
}
