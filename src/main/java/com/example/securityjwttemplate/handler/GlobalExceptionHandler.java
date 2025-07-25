package com.example.securityjwttemplate.handler;

import com.example.securityjwttemplate.dto.response.ErrorResponse;
import com.example.securityjwttemplate.exception.ApplicationException;
import com.example.securityjwttemplate.util.ErrorResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
        logger.error("Unexpected error: ", exception);
        ErrorResponse errorResponse = ErrorResponseFactory.create(500, "Internal Server Error.");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        ErrorResponse errorResponse = ErrorResponseFactory.create(400, errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


}
