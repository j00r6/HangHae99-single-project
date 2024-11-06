package com.earlybird.userservice.Global.handler;

import com.earlybird.userservice.Global.exception.IllegalToken;
import com.earlybird.userservice.Global.exception.InvalidCredentialsException;
import com.earlybird.userservice.Global.exception.TokenExpiredException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Message> handleBadRequest(BadRequestException e) {
        Message message = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(message);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Message> handleCredential(InvalidCredentialsException e) {
        Message message = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Message> handleTokenExpire(TokenExpiredException e) {
        Message message = new Message(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(message, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalToken.class)
    public ResponseEntity<Message> handleIllegalToken(IllegalToken e) {
        Message message = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }
}