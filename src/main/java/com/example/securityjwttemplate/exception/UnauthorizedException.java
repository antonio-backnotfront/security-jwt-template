package com.example.securityjwttemplate.exception;

public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(String msg) {
        super(msg, 401);
    }
}
