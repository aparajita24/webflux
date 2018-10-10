package com.test.appy.springdemo.webflux;

import com.test.appy.springdemo.webflux.model.Comment;
import com.test.appy.springdemo.webflux.utils.EmbeddedPostgresRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebfluxDemoApplication.class, webEnvironment = RANDOM_PORT,
        properties = {
                "postgres.config.host=localhost",
                "postgres.config.port=12345",
                "postgres.config.database=test-db",
                "postgres.config.username=test-user",
                "postgres.config.password=abcd1234",
        }
)
@AutoConfigureWebTestClient(timeout = "36000")
@Slf4j
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StressTest {

    @ClassRule
    public static final EmbeddedPostgresRule POSTGRES_RULE = EmbeddedPostgresRule.builder()
            .host("localhost")
            .port(12345)
            .database("test-db")
            .username("test-user")
            .password("abcd1234")
            .build();

    @Autowired
    private WebTestClient client;

    @LocalServerPort
    private int serverPort;

    @Before
    public void setUp() {
        client = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + serverPort + "/comments")
                .build();
    }

    @Test
    public void test01GetCommentsWith10Requests() {
        testGetComments(10);
    }

    @Test
    public void test02GetCommentsWith50Requests() {
        testGetComments(50);
    }

    @Test
    @Ignore
    public void test03GetCommentsWith99Requests() {
        testGetComments(99);
    }

    @Test
    @Ignore
    public void test04GetCommentsWith100Requests() {
        testGetComments(100);
    }

    @Test
    @Ignore
    public void test05GetCommentsWith101Requests() {
        testGetComments(101);
    }

    @Test
    @Ignore
    public void test06GetCommentsWith150Requests() {
        testGetComments(150);
    }

    private void testGetComments(final int numberOfRequests) {
        final List<Integer> failedAttempts = new ArrayList<>();
        final AtomicInteger successfulAttempts = new AtomicInteger();

        IntStream.range(0, numberOfRequests).parallel()
                .forEach(attempt -> {
                    try {
                        client.get()
                                .exchange()
                                .expectStatus().isOk()
                                .expectBodyList(Comment.class).hasSize(10);

                        successfulAttempts.incrementAndGet();
                    } catch (final AssertionError ex) {
                        failedAttempts.add(attempt);
                    }
                });

        for (final int failedAttempt : failedAttempts) {
            log.error("Failed attempt: {}", failedAttempt);
        }

        assertThat(successfulAttempts.get(), is(numberOfRequests));
        assertThat(failedAttempts, empty());
    }

}