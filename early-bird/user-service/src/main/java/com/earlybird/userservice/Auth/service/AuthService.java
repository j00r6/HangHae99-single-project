package com.earlybird.userservice.Auth.service;

import com.earlybird.userservice.Global.exception.IllegalToken;
import com.earlybird.userservice.Global.exception.UserNotFoundException;
import com.earlybird.userservice.JWT.service.TokenService;
import com.earlybird.userservice.Redis.RedisJwtUtil;
import com.earlybird.userservice.User.entity.User;
import com.earlybird.userservice.User.repository.UserRepository;
import com.earlybird.userservice.Auth.dto.LogInRequest;
import com.earlybird.userservice.JWT.provider.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder managerBuilder;
    private final TokenService tokenService;
    private final RedisJwtUtil redisJwtUtil;

    public void login(LogInRequest request, HttpServletResponse headers) {
        log.info("로그인 요청 이메일 : " + request.getEmail());
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException(request.getEmail() + " 해당 계정의 가입 정보를 확인할 수 없습니다. 회원가입을 진행해 주세요"));

        /*
         * 최초 로그인 시 사용자의 이메일과 비밀번호를 받아서 인증하는 과정을 처리
         * AuthenticationManagerBuilder 를 사용하여 AuthenticationManager 를 가져오고,
         * UsernamePasswordAuthenticationToken 객체를 생성하여 사용자 정보를 담고,
         * authenticate() 메서드를 통해 인증을 수행한 후, 인증 결과를 Authentication 객체로 반환
         */

        //Authentication Manager 에서 인증된 유저객체 가져오기
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);

        //accessToken 생성하기
        String accessToken = tokenProvider.generateAccessToken(authentication);
        log.info("accessToken 발급 확인 (service) : " + accessToken);

        //refreshToken 생성하기
        headers.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        //Redis 에서 refreshToken 관리하기
        tokenService.saveRefreshToken(request.getEmail(), refreshToken);
    }

    public void logout(Long userId) {

        if (userId == null) {
            throw new IllegalToken("토큰 정보가 유효하지 않습니다.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("회원 정보가 존재하지 않습니다."));

        String email = user.getEmail();

        //로그아웃시 JWT 를 관리하는 redisJwtUtil 에서 refreshToken 을 블랙리스트 처리
        redisJwtUtil.addToBlacklist(email);
        tokenService.deleteRefreshToken(email);
        userRepository.deleteById(userId);

    }

    public User findVerifyUser (Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        User findUserById =
                optionalUser.orElseThrow(() ->
                        new UserNotFoundException("회원 정보가 존재하지 않습니다"));
        return findUserById;
    }

    public User findUserByPrincipal(String principal) {
        Optional<User> optionalUser = userRepository.findByEmail(principal);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("회원 정보가 존재하지 않습니다");
        }
        return optionalUser.get();
    }
}
