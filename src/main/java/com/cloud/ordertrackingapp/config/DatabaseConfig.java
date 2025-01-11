package com.cloud.ordertrackingapp.config;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

@Configuration(value = "databaseConfig")
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.cloud.ordertrackingapp.dao"}
)
public class DatabaseConfig {

    @Value("${WRITE_HOST}")
    private String masterUrl;
    @Value("${WRITE_USERNAME}")
    private String masterUser;
    @Value("${WRITE_PASSWORD}")
    private String masterPassword;

    @Value("${READ_HOST}")
    private String slaveUrl;
    @Value("${READ_USERNAME}")
    private String slaveUser;
    @Value("${READ_PASSWORD}")
    private String slavePassword;

    @Primary
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        String format = String.format("master date: url= %s, user= %s, password= %s", masterUrl, masterUser, masterPassword);
        System.out.println(format);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUser);
        dataSource.setPassword(masterPassword);
        return dataSource;
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        String format = String.format("slave date: url= %s, user= %s, password= %s", slaveUrl, slaveUser, slavePassword);
        System.out.println(format);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(slaveUrl);
        dataSource.setUsername(slaveUser);
        dataSource.setPassword(slavePassword);
        return dataSource;
    }

    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("masterDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.cloud.ordertrackingapp.entity")
                .persistenceUnit("master")
                .build();
    }


    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("entityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean(name = "secondEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean secondEntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("slaveDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.cloud.ordertrackingapp.entity")
                .persistenceUnit("slave")
                .build();
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
