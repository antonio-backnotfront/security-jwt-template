package com.example.securityjwttemplate.util;

import com.example.securityjwttemplate.dto.response.ErrorResponse;

public class ErrorResponseFactory {
    public static ErrorResponse create(int status, String error){
        return new ErrorResponse(status, error, System.currentTimeMillis());
    }
}
