package com.earlybird.userservice.Security.resolver;

import com.earlybird.userservice.Auth.service.AuthService;
import com.earlybird.userservice.Global.exception.UserNotFoundException;
import com.earlybird.userservice.User.entity.User;
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
public class LoginUserIdArgumentResolver implements HandlerMethodArgumentResolver {
    private final AuthService authService;


    public LoginUserIdArgumentResolver(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        parameter.getParameterAnnotations();
        return true;
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getName(); // 사용자 인증 정보
        log.info(principal.toString() + " principal");

        // 익명이면 -1L 리턴
        if ("anonymousUser".equals(principal)) {
            throw new UserNotFoundException("존재하지 않는 회원정보 입니다");
        }

        User user = authService.findUserByPrincipal(principal.toString());

        return user.getUserId();
    }
}
