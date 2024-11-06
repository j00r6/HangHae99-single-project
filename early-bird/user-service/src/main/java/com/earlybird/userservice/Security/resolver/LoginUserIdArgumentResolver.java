package com.earlybird.userservice.Security.resolver;

import com.earlybird.userservice.JWT.filter.JwtFilter;
import com.earlybird.userservice.JWT.provider.TokenProvider;
import com.earlybird.userservice.User.entity.User;
import com.earlybird.userservice.User.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userservice;
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        parameter.getParameterAnnotations();
        return true;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = getJwtFromRequest(webRequest);

        if (token != null && !token.isEmpty()) {
            String username = String.valueOf(tokenProvider.parseClaims(token));
            log.info("Username from JWT: " + username);

            // 익명이면 -1L 리턴
            if ("anonymousUser".equals(username)) {
                return -1L;
            }

            User user = userservice.findUserByPrincipal(username);
            return user.getUserId();
        }

        return -1L; // 토큰이 없거나 오류 발생시 -1L 리턴
    }

    private String getJwtFromRequest(NativeWebRequest webRequest) {
        String bearerToken = webRequest.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 이후의 부분
        }
        return null;
    }
}
