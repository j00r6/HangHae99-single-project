package com.single_project.early_bird.Redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisJwtUtil {
    private final RedisTemplate<String, String> template;

    @Value("${spring.data.redis.jwtDuration}")
    private int jwtDuration;

    // 특정 키의 값을 가져옵니다.
    public String getData(String key) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        return valueOperations.get(key);
    }

    // 특정 키가 Redis에 존재하는지 확인합니다.
    public boolean existData(String key) {
        return Boolean.TRUE.equals(template.hasKey(key));
    }

    // JWT 토큰을 Redis에 저장하고 만료 시간을 설정합니다.
    public void setDataExpire(String key, String value) {
        ValueOperations<String, String> valueOperations = template.opsForValue();
        Duration expireDuration = Duration.ofSeconds(jwtDuration);
        valueOperations.set(key, value, expireDuration);
    }

    // 특정 키의 데이터를 삭제합니다.
    public void deleteData(String key) {
        template.delete(key);
    }

    // JWT 토큰을 저장하는 기능: 이미 데이터가 존재하면 삭제하고 새로 저장
    public void createRedisData(String tokenKey, String tokenValue) {
        if (existData(tokenKey)) {
            deleteData(tokenKey); // 기존 데이터가 있으면 삭제
        }
        setDataExpire(tokenKey, tokenValue); // 새로 데이터를 저장하면서 만료 시간 설정
    }
}
