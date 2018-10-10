package com.test.appy.springdemo.webflux.configuration;

import com.test.appy.springdemo.webflux.data.PostgresRepository;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(PostgresConnectionProperties.class)
public class PostgresConfiguration {

    @Bean
    public PostgresRepository postgresRepo(final PostgresConnectionProperties connectionProperties) {

        final PostgresqlConnectionFactory connectionFactory = new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(connectionProperties.getHost())
                        .port(connectionProperties.getPort())
                        .database(connectionProperties.getDatabase())
                        .username(connectionProperties.getUsername())
                        .password(connectionProperties.getPassword())
                        .build());

        return new PostgresRepository(connectionFactory);
    }

}