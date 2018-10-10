package com.test.appy.springdemo.webflux.utils;

import lombok.Builder;
import lombok.Getter;
import org.junit.rules.ExternalResource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;

import java.sql.DriverManager;
import java.sql.SQLException;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

@Builder
public class EmbeddedPostgresRule extends ExternalResource {

    // Exclude this from builder
    private static EmbeddedPostgres POSTGRES = new EmbeddedPostgres(V9_6);

    @Getter
    private String host;

    @Getter
    private int port;

    @Getter
    private String database;

    @Getter
    private String username;

    @Getter
    private String password;

    @Override
    protected void before() throws Exception {
        final String url = POSTGRES.start(host, port, database, username, password);

        CommentsUpload.createAndPopulateCommentTable(() -> {
            try {
                return DriverManager.getConnection(url);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    protected void after() {
        POSTGRES.stop();
    }

}