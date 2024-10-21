package com.single_project.early_bird.Global.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialsException extends RuntimeException{

    public InvalidCredentialsException(String message) {
        super(message);
    }
}

