package com.example.cloud_api_v3.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorInputDataException extends RuntimeException {

    public ErrorInputDataException(String message) {
        super(message);
    }
}
