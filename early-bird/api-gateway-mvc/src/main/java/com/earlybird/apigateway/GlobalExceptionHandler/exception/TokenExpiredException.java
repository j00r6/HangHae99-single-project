package com.earlybird.apigateway.GlobalExceptionHandler.exception;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String message) {
        super(message);
    }
}