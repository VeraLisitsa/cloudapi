package com.example.cloud_api_v3.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorGettingFileListException extends RuntimeException{
    public ErrorGettingFileListException(String message) {
        super(message);
    }
}
