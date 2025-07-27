package com.example.securityjwttemplate.model.enums;

import io.jsonwebtoken.JwtException;

public enum TokenType {
    ACCESS, REFRESH;

    public static TokenType fromString(String name) {
        try {
            return TokenType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JwtException("Invalid token type: " + name);
        }
    }
}
