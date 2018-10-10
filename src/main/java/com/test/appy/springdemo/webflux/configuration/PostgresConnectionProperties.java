package com.test.appy.springdemo.webflux.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "postgres.configuration", ignoreUnknownFields = false)
@Data
public class PostgresConnectionProperties {

    private String host;

    private int port;

    private String database;

    private String username;

    private String password;

}