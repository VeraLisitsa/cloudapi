package com.example.cloud_api_v3.advice;

import com.example.cloud_api_v3.dto.ErrorDto;
import com.example.cloud_api_v3.entity.ErrorEntity;
import com.example.cloud_api_v3.exception.*;
import com.example.cloud_api_v3.util.MappingDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionHandlerAdvice {

    private final MappingDto mappingDto;

    @ExceptionHandler
    public ResponseEntity<ErrorDto> unauthorizedErrorExceptionHandler(UnauthorizedErrorException e) {
        return new ResponseEntity<>(createErrorDto(e), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> errorInputDataExceptionHandler(ErrorInputDataException e) {
        return new ResponseEntity<>(createErrorDto(e), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> errorUploadFileExceptionHandler(ErrorUploadFileException e) {
        return new ResponseEntity<>(createErrorDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> errorGettingFileListExceptionHandler(ErrorGettingFileListException e) {
        return new ResponseEntity<>(createErrorDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> errorDeleteFileExceptionHandler(ErrorDeleteFileException e) {
        return new ResponseEntity<>(createErrorDto(e), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler
    public ResponseEntity<ErrorDto> handlerMethodValidationExceptionHandler(HandlerMethodValidationException e) {
        ErrorEntity error = new ErrorEntity("Error input data");
        return new ResponseEntity<>(mappingDto.errorEntityToErrorDto(error), HttpStatus.BAD_REQUEST);
    }


    protected ErrorDto createErrorDto(RuntimeException e) {
        ErrorEntity errorEntity = new ErrorEntity(e.getMessage());
        return mappingDto.errorEntityToErrorDto(errorEntity);
    }
}
