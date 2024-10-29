package com.earlybird.orderservice.Global.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

