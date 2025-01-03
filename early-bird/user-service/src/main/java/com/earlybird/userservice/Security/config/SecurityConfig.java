package com.earlybird.userservice.Security.config;

import com.earlybird.userservice.Global.exception.AccessDenied;
import com.earlybird.userservice.Global.exception.AuthenticationFailed;
import com.earlybird.userservice.JWT.filter.JwtFilter;
import com.earlybird.userservice.JWT.provider.TokenProvider;
import com.earlybird.userservice.Redis.RedisJwtUtil;
import com.earlybird.userservice.Security.userdetails.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final RedisJwtUtil jwtUtil;
    private final AuthenticationFailed authenticationFailed;
    private final AccessDenied accessDenied;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain jwtConfig (HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .formLogin(AbstractHttpConfigurer::disable)
                // formLogin 비활성화 대체 가능 .formLogin((formLogin) -> formLogin.disable())
                // 로컬 개발일 경우 sameOrigin 사용하여 동일출처 허용
                .httpBasic(AbstractHttpConfigurer::disable) //

                //토큰 방식 활용을 위해 Session Stateless 설정
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                /**
                 * MEMBER 의 인증/인가 구현
                 * 권한에 따른 페이지 구분을 구현할 경우
                 * Member Entity 상에서 Enum 으로 구분할 때
                 * ROLE_"권한" 양식을 사용해야 Spring Security 에서 인식을 한다.
                 */

//                .authorizeHttpRequests((auth) -> auth
//                        // 회원가입이나 로그인시 인증정보 없이 접근가능
//                        .requestMatchers("/auth/login", "/user/login").permitAll()
//                        .anyRequest().authenticated()
//
//                )

                .authorizeHttpRequests((auth) -> auth
                        .anyRequest().permitAll()

                )

                .userDetailsService(userDetailsService)

//                .addFilterBefore(new JwtFilter(tokenProvider, jwtUtil), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JwtFilter(tokenProvider, jwtUtil), ExceptionTranslationFilter.class)
                .exceptionHandling((eh) -> eh
                        .authenticationEntryPoint(authenticationFailed)
                        .accessDeniedHandler(accessDenied)
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    public static PasswordEncoder createDelegatingPasswordEncoder() {
        String encodingId = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(encodingId, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(encodingId, encoders);
    }
}
