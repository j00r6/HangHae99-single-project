package com.earlybird.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter {

    private final WebClient webClient = WebClient.create();

    @Value("${auth.service.uri}")
    private String authServiceUri = ; // 인증/인가 서비스 URI

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestPath = exchange.getRequest().getPath().toString();

        // 특정 경로에 대해서만 JWT 검증 적용
        if (requestPath.contains("/favorite") && requestPath.contains("/orders") && requestPath.contains("/user/logout")) {
            String jwtToken = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (jwtToken == null || jwtToken.isEmpty()) {
                // JWT가 없는 경우 요청 차단
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            // JWT가 있는 경우 인증/인가 서비스로 사용자 ID 확인 요청
            return webClient.get()
                    .uri(authServiceUri)
                    .header(HttpHeaders.AUTHORIZATION, jwtToken)
                    .retrieve()
                    .bodyToMono(String.class) // 사용자 ID가 문자열로 반환된다고 가정
                    .flatMap(userId -> {
                        // 사용자 ID가 반환되면 헤더에 추가하고 최종 서비스로 라우팅
                        ServerWebExchange modifiedExchange = exchange.mutate()
                                .request(r -> r.header("X-User-Id", userId)) // 최종 서비스에 사용자 ID 전달
                                .build();
                        return chain.filter(modifiedExchange);
                    })
                    .onErrorResume(e -> {
                        // 인증 실패 시 에러 반환
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                        return exchange.getResponse().setComplete();
                    });
        }

        // JWT가 필요하지 않은 경로는 그대로 통과
        return chain.filter(exchange);
    }
}
