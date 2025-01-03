package com.earlybird.userservice.JWT.filter;

import com.earlybird.userservice.Redis.RedisJwtUtil;
import com.earlybird.userservice.JWT.provider.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    private final TokenProvider tokenProvider;
    private final RedisJwtUtil redisJwtUtil;

    // 실제 필터릴 로직
    // 토큰의 인증정보를 SecurityContext에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();

        try{

            if (StringUtils.hasText(jwt) && tokenProvider.validateAccessToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 이메일과 권한 정보 Request 에 추가, 추후 @RequestAttribute 사용을 위해
                request.setAttribute("username", authentication.getPrincipal());
                request.setAttribute("role", authentication.getAuthorities());
                request.setAttribute("userId", authentication.getPrincipal());

                log.info("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }

        } catch (Exception e) {
            redisJwtUtil.isTokenBlacklisted(jwt);
            log.error("토큰이 유효하지 않습니다, uri: {}", requestURI);
            log.error("Exception : " + e);
            log.error("Message : " + e.getMessage());

//            request.setAttribute("exception", e);
            throw new AuthenticationException(e.getMessage()) {};
        }

        filterChain.doFilter(request, response); // 다음 필터로 요청 전달
    }

    // Request Header 에서 토큰 정보를 꺼내오기 위한 메소드
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }


}
