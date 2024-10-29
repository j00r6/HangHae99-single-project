package com.earlybird.orderservice.Global.exception;

public class IllegalToken extends RuntimeException {
    public IllegalToken(String message) {
        super(message);
    }
}
