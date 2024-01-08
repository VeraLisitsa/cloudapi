package com.example.cloud_api_v3.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ErrorDeleteFileException extends RuntimeException{
    public ErrorDeleteFileException(String message){
        super(message);
    }
}
