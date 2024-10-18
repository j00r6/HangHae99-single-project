package com.single_project.early_bird.Global.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    public BadRequestException(String message) {
        super(message);
    }
}

