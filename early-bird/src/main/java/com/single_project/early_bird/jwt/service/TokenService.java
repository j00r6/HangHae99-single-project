package com.single_project.early_bird.jwt.service;

import com.single_project.early_bird.Redis.RedisJwtUtil;
import com.single_project.early_bird.jwt.dto.LogInRequest;
import com.single_project.early_bird.jwt.provider.TokenProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {
    private final RedisJwtUtil redisJwtUtil;

    public void saveRefreshToken(String username, String refreshToken) {
        String refreshTokenKey = "refresh_token:" + username;
        redisJwtUtil.createRedisData(refreshTokenKey, refreshToken);
    }

    public String getRefreshToken(String username) {
        String refreshTokenKey = "refresh_token:" + username;
        return redisJwtUtil.getData(refreshTokenKey);
    }

    public void deleteRefreshToken(String username) {
        String refreshTokenKey = "refresh_token:" + username;
        redisJwtUtil.deleteData(refreshTokenKey);
    }
}
