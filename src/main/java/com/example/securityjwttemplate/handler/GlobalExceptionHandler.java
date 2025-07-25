package com.example.securityjwttemplate.handler;

import com.example.securityjwttemplate.dto.response.ErrorResponse;
import com.example.securityjwttemplate.exception.ApplicationException;
import com.example.securityjwttemplate.util.ErrorResponseFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> handleException(ApplicationException exception) {
        ErrorResponse errorResponse = ErrorResponseFactory.create(exception.getStatusCode(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(exception.getStatusCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception) {
        logger.error(Arrays.toString(exception.getStackTrace()));
        ErrorResponse errorResponse = ErrorResponseFactory.create(500, "Internal Server Error.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
