package com.test.appy.springdemo.webflux.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@EqualsAndHashCode
public class Comment {

    @Size(max = 20)
    private final String id;

    @Size(max = 140)
    private final String text;

    private final LocalDate createdTime;

    @JsonCreator
    public Comment(@JsonProperty("id") String id,
                   @JsonProperty("text") String text,
                   @JsonProperty("createdTime") LocalDate createdTime) {
        this.id = id;
        this.text = text;
        this.createdTime = createdTime;
    }

}