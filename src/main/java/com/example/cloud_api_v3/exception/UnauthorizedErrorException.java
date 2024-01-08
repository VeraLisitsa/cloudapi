package com.example.cloud_api_v3.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UnauthorizedErrorException extends RuntimeException {

    public UnauthorizedErrorException(String message) {
        super(message);
    }

}
