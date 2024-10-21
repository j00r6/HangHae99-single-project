package com.single_project.early_bird.Global.handler;

import com.single_project.early_bird.Global.exception.IllegalToken;
import com.single_project.early_bird.Global.exception.InvalidCredentialsException;
import com.single_project.early_bird.Global.exception.TokenExpiredException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Message> handle(BadRequestException e) {
        Message message = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Message> handle(InvalidCredentialsException e) {
        Message message = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Message> handle(TokenExpiredException e) {
        Message message = new Message(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalToken.class)
    public ResponseEntity<Message> handle(IllegalToken e) {
        Message message = new Message(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }
}