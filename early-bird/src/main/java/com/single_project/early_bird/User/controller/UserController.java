package com.single_project.early_bird.User.controller;

import com.single_project.early_bird.User.dto.SignInRequest;
import com.single_project.early_bird.User.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
