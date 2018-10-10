package com.test.appy.springdemo.webflux.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class CommentsUpload {

    /**
     * creates the default table 'Comment',
     * populates 10 entries in the table
     */
    public static void createAndPopulateCommentTable(final Supplier<Connection> connectionSupplier) {

        try (final Connection connection = connectionSupplier.get()) {
            try (final Statement createCommentsTable = connection.createStatement()) {
                createCommentsTable.execute("CREATE TABLE comment(id VARCHAR (20) PRIMARY KEY, " +
                        "text VARCHAR (140), createdTime DATE);");
            }

            IntStream.range(0, 10).forEach(i -> {
                try (final Statement insertCommentStatement = connection.createStatement()) {
                    insertCommentStatement.execute(
                            "INSERT INTO comment(id, text, createdTime) " +
                                    "VALUES ('" + UUID.randomUUID().toString()
                                    .substring(0, 20) + "', 'test comment" + i + "', '2018-10-08')");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}