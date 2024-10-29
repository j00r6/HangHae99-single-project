package com.earlybird.userservice.Global.exception;

public class IllegalToken extends RuntimeException {
    public IllegalToken(String message) {
        super(message);
    }
}
