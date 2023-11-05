package org.themarioga.cclh.commons;

import com.github.springtestdbunit.bean.DatabaseConfigBean;
import com.github.springtestdbunit.bean.DatabaseDataSourceConnectionFactoryBean;
import jakarta.persistence.EntityManagerFactory;
import org.dbunit.ext.h2.H2DataTypeFactory;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@Configuration
@ComponentScan("org.themarioga.cclh")
@EntityScan(basePackages = {"org.themarioga.cclh"})
@ComponentScan(basePackages = {"org.themarioga.cclh"})
@EnableJpaRepositories(basePackages = "org.themarioga.cclh.commons.dao")
public class TestConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public DatabaseDataSourceConnectionFactoryBean dbUnitDatabaseConnection() {
        DatabaseConfigBean bean = new DatabaseConfigBean();
        bean.setDatatypeFactory(new H2DataTypeFactory());

        DatabaseDataSourceConnectionFactoryBean dbConnectionFactory = new DatabaseDataSourceConnectionFactoryBean(dataSource);
        dbConnectionFactory.setDatabaseConfig(bean);
        return dbConnectionFactory;
    }

}
