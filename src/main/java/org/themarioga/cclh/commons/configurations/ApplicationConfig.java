package org.themarioga.cclh.commons.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:properties/${spring.profiles.active}/application-config.properties")
public class ApplicationConfig {
}
