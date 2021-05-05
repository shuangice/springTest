package com.example.datasourcedemo;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

abstract class DataSourceConfiguration {
    @ConditionalOnClass(HikariDataSource.class)
    @ConditionalOnMissingBean(DataSource.class)
    @ConditionalOnProperty(name = "spring.datasource.type", havingValue = "com.zaxxer.hikari.HikariDataSource",
            matchIfMissing = true)
    static class Hikari{

        @Bean
        @ConfigurationProperties(prefix = "spring.datasource.hikari")
        public HikariDataSource hikariDataSource(DataSourceProperties properties) {
            HikariDataSource dataSource = CreateDataSource(properties, HikariDataSource.class);
            if (StringUtils.hasText(properties.getName())) {
                dataSource.setPoolName(properties.getName());
            }
            return dataSource;
        }

        protected static <T> T CreateDataSource(DataSourceProperties properties,  Class<? extends DataSource> type) {
            return (T) properties.initializeDataSourceBuilder().type(type).build();
        }

    }
}
