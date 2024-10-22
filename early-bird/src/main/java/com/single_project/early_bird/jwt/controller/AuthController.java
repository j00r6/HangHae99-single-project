package com.single_project.early_bird.jwt.controller;

import com.single_project.early_bird.jwt.dto.LogInRequest;
import com.single_project.early_bird.jwt.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> userLogin (@RequestBody LogInRequest request,HttpServletResponse responseHeader) { //<TokenDto.response>
        authService.login(request, responseHeader);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("로그인 완료");
    }
}
