package com.ets.challenge.config;

import com.ets.challenge.dto.ErrorResponse;
import com.ets.challenge.exception.FileCopyException;
import com.ets.challenge.exception.FileInvalidExtensionException;
import com.ets.challenge.exception.FileNotFoundException;
import com.ets.challenge.exception.FileStoreException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value = {FileInvalidExtensionException.class, FileNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleClientException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(value = {FileCopyException.class, FileStoreException.class})
    protected ResponseEntity<ErrorResponse> handleServerException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
