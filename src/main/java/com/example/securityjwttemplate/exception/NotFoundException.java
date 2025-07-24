package com.example.securityjwttemplate.exception;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String msg) {
        super(msg, 404);
    }
}
