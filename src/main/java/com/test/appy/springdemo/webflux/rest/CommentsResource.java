package com.test.appy.springdemo.webflux.rest;

import com.test.appy.springdemo.webflux.data.PostgresRepository;
import com.test.appy.springdemo.webflux.model.Comment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController("/comments")
public class CommentsResource {

    private final PostgresRepository postgresRepo;

    public CommentsResource(final PostgresRepository postgresRepo) {
        this.postgresRepo = postgresRepo;
    }

    @GetMapping
    public Flux<Comment> streamComments() {
        return postgresRepo.getAllCommentsWithFlux();
    }

}