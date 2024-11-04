package com.earlybird.apigateway.GlobalExceptionHandler.exception;

public class IllegalToken extends RuntimeException {
    public IllegalToken(String message) {
        super(message);
    }
}
