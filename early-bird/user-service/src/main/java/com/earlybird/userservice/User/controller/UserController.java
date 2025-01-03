package com.earlybird.userservice.User.controller;

import com.earlybird.userservice.Global.exception.BadRequestException;
import com.earlybird.userservice.Global.handler.Message;
import com.earlybird.userservice.User.dto.SignInRequest;
import com.earlybird.userservice.User.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity memberSignIn(@RequestBody SignInRequest request) {

        try {
            String response = userService.signInUser(request);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);

        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new Message("회원정보가 이미 존재합니다.", HttpStatus.BAD_REQUEST));
        }

    }
}
