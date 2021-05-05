package com.example.datasourcedemo;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;


@Configuration
public class DataConfig {
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebService() throws SQLException {
        return Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort","8082");
    }
}
