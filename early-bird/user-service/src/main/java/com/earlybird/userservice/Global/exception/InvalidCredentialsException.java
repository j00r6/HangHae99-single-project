package com.earlybird.userservice.Global.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

