package com.example.cloud_api_v3.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ErrorEntity {


    private String message;

    @EqualsAndHashCode.Include
    private int id;

    private static final AtomicInteger count = new AtomicInteger(0);

    public ErrorEntity(String message) {
        this.message = message;
        this.id = count.incrementAndGet();
    }

}
