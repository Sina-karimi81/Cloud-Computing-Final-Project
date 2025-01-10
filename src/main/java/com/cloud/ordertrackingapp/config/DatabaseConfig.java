package com.cloud.ordertrackingapp.config;

import ch.qos.logback.core.util.StringUtil;
import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager",
        basePackages = {"com.cloud.ordertrackingapp.dao"}
)
@Slf4j
public class DatabaseConfig {

    @Value("${spring.datasource.master.url}")
    private String masterUrl;
    @Value("${spring.datasource.master.username}")
    private String masterUser;
    @Value("${spring.datasource.master.password}")
    private String masterPassword;
    @Value("${master.hibernate.ddl-auto}")
    private String hibernateDdlAuto;

    @Value("${spring.datasource.slave.url}")
    private String slaveUrl;
    @Value("${spring.datasource.slave.username}")
    private String slaveUser;
    @Value("${spring.datasource.slave.password}")
    private String slavePassword;
    @Value("${slave.hibernate.ddl-auto}")
    private String slaveHibernateDdlAuto;

    @Primary
    @Bean(name = "masterDataSource")
    public DataSource masterDataSource() {
        String format = String.format("master url: {} , master user: {} , master password: {}", masterUrl, masterUser, masterPassword);
        System.out.println(format);
        log.debug("master url: {} , master user: {} , master password: {}", masterUrl, masterUser, masterPassword);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl(masterUrl);
        dataSource.setUsername(masterUser);
        dataSource.setPassword(masterPassword);
        return dataSource;
    }

    @Bean(name = "slaveDataSource")
    public DataSource slaveDataSource() {
        String format = String.format("slave url: {} , slave user: {} , slave password: {}", masterUrl, masterUser, masterPassword);
        System.out.println(format);
        log.debug("slave url: {} , slave user: {} , slave password: {}", masterUrl, masterUser, masterPassword);
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
                .properties(Map.of(Environment.HBM2DDL_AUTO, hibernateDdlAuto))
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
                .properties(Map.of(Environment.HBM2DDL_AUTO, slaveHibernateDdlAuto))
                .build();
    }

    @Bean(name = "secondTransactionManager")
    public PlatformTransactionManager secondTransactionManager(
            @Qualifier("secondEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

}
