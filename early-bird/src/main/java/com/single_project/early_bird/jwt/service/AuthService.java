package com.single_project.early_bird.jwt.service;

import com.single_project.early_bird.Global.exception.UserNotFoundException;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.repository.UserRepository;
import com.single_project.early_bird.jwt.dto.LogInRequest;
import com.single_project.early_bird.jwt.provider.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenService tokenService;

    public void login(LogInRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail() + " 해당 계정의 가입 정보를 확인할 수 없습니다. 회원가입을 진행해 주세요"));

        /*
         * 최초 로그인 시 사용자의 이메일과 비밀번호를 받아서 인증하는 과정을 처리
         * AuthenticationManagerBuilder 를 사용하여 AuthenticationManager 를 가져오고,
         * UsernamePasswordAuthenticationToken 객체를 생성하여 사용자 정보를 담고,
         * authenticate() 메서드를 통해 인증을 수행한 후, 인증 결과를 Authentication 객체로 반환
         */
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        String accessToken = tokenProvider.generateAccessToken(authentication);
        response.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        String refreshToken = tokenProvider.generateRefreshToken(authentication);
        response.setHeader("X-Refresh-Token", refreshToken);

        tokenService.saveRefreshToken(request.getEmail(), refreshToken);
    }
}
