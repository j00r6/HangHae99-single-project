package com.earlybird.userservice.User.controller;

import com.earlybird.userservice.Security.resolver.LoginUserId;
import com.earlybird.userservice.User.dto.SignInRequest;
import com.earlybird.userservice.User.entity.User;
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
    public ResponseEntity<String> memberSignIn(@RequestBody SignInRequest request) {

        userService.signInUser(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("회원가입 완료");
    }

    @GetMapping
    public ResponseEntity<Long> getUser(@LoginUserId Long userId) {
        User findUser = userService.findVerifyUser(userId);
        Long returnUserId = findUser.getUserId();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(returnUserId);
    }
}
