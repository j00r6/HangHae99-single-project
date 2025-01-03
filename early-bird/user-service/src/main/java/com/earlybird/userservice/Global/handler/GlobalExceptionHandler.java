package com.earlybird.userservice.Global.handler;

import com.earlybird.userservice.Global.exception.*;
import io.jsonwebtoken.security.SignatureException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Message> handleBadRequest(BadRequestException e) {
        Message errormessage = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errormessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Message> handleCredential(InvalidCredentialsException e) {
        Message errormessage = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errormessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Message> handleTokenExpire(TokenExpiredException e) {
        Message errormessage = new Message(e.getMessage(), HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(errormessage, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IllegalToken.class)
    public ResponseEntity<Message> handleIllegalToken(IllegalToken e) {
        Message errormessage = new Message(e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(errormessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Message> handleUserNotFound(UserNotFoundException e) {
        Message errormessage = new Message(e.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(errormessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Message> handleAuthenticationException(AuthenticationException e) {
        Message errormessage = new Message(e.getMessage(), HttpStatus.UNAUTHORIZED);
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return new ResponseEntity<>(errormessage, httpStatus);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Message> handleAccessDeniedException() {
        Message errormessage = new Message("토큰 정보가 잘못됐습니다.", HttpStatus.FORBIDDEN);
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(errormessage, httpStatus);
    }
}