package com.example.cloud_api_v3.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorUploadFileException extends RuntimeException{

    public ErrorUploadFileException(String message) {
        super(message);
    }
}
