package com.example.securityjwttemplate.exception;

public class BadRequestException extends ApplicationException {
    public BadRequestException(String msg) {
        super(msg, 400);
    }
}
