package com.example.securityjwttemplate.exception;


public class ApplicationException extends RuntimeException {
    private final int statusCode;

    public ApplicationException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
