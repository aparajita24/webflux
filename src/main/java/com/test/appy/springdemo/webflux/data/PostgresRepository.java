package com.test.appy.springdemo.webflux.data;


import com.test.appy.springdemo.webflux.model.Comment;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.PostgresqlResult;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public class PostgresRepository {

    private PostgresqlConnectionFactory connection;

    public PostgresRepository(PostgresqlConnectionFactory connection) {
        this.connection = connection;
    }

    /**
     * transforms result per row to Comment object
     *
     * @param result flux comment
     * @return comment
     */
    private static Flux<Comment> extractColumns(PostgresqlResult result) {
        return result
                .map((row, rowMetadata) ->
                        new Comment(row.get("id").toString(),
                                (String) row.get("text"),
                                (LocalDate) row.get("createdtime")
                        ));
    }

    /**
     * reads comments from the database table - comment using r2dbc postgres
     *
     * @return flux comments
     */
    public Flux<Comment> getAllCommentsWithFlux() {
        return connection
                .create()
                .flatMapMany(x -> x.createStatement("select * from comment")
                        .execute()
                        .flatMap(PostgresRepository::extractColumns));
    }

}