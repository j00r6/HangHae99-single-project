package com.earlybird.userservice.Auth.controller;

import com.earlybird.userservice.Global.exception.IllegalToken;
import com.earlybird.userservice.Global.exception.TokenExpiredException;
import com.earlybird.userservice.Global.exception.UserNotFoundException;
import com.earlybird.userservice.Global.handler.Message;
import com.earlybird.userservice.Security.resolver.LoginUserId;
import com.earlybird.userservice.Auth.dto.LogInRequest;
import com.earlybird.userservice.Auth.service.AuthService;
import com.earlybird.userservice.User.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity userLogin (@RequestBody LogInRequest request, HttpServletResponse response) {
        String email = request.getEmail();

        try {
            authService.login(request, response);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("로그인 완료");

        } catch (UserNotFoundException e) {
            log.error("로그인을 시도한 {} 에 대한 회원정보가 존재하지 않습니다", email);
            throw new UserNotFoundException("로그인을 시도한 " + email + "에 대한 회원정보가 존재하지 않습니다");
        }

    }

    @PostMapping("/logout")
    public ResponseEntity userLogout (@LoginUserId Long userId) {

        try {
            authService.logout(userId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body("로그아웃 완료");

        } catch (IllegalToken e) {
            throw new IllegalToken("유효하지 않은 토큰으로 접근을 요청했습니다.");

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("회원 정보가 존재하지 않습니다.");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity getUser(@LoginUserId Long userId) {
        try {
            authService.findVerifyUser(userId);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .header("X-User-Id", String.valueOf(userId))
                    .body(String.valueOf(userId));

        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("회원 정보가 존재하지 않습니다.");

        } catch (AuthenticationException e) {
            throw new AuthenticationException(e.getMessage()) {};
        }
    }
}
