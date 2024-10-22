package com.single_project.early_bird.jwt.controller;

import com.single_project.early_bird.Security.resolver.LoginUserId;
import com.single_project.early_bird.jwt.dto.LogInRequest;
import com.single_project.early_bird.jwt.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
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
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> userLogin (@RequestBody LogInRequest request, HttpServletResponse response) {
        authService.login(request, response);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("로그인 완료");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> userLogout (@LoginUserId Long userId) {
        authService.logout(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("로그아웃 완료");
    }
}
