package com.earlybird.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final WebClient webClient = WebClient.create();

    public JwtAuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String requestPath = exchange.getRequest().getPath().toString();

            // 특정 경로에 대해서만 JWT 검증 적용
            if (requestPath.contains("/favorite") || requestPath.contains("/orders") || requestPath.contains("/user/logout") || requestPath.contains("user/getUserId")) {
                String requestJwt = exchange.getRequest().getHeaders().getFirst("Authorization");

                if (requestJwt == null || requestJwt.isEmpty()) {
                    // JWT가 없는 경우 요청 차단
                    return handleMissingJwt(exchange);
                }

                // JWT가 있는 경우 인증/인가 서비스로 사용자 ID 확인 요청
                return verifyJwtAndGetUserId(requestJwt)
                        .flatMap(responseEntity -> {
                            String userId = responseEntity.getHeaders().getFirst("X-User-Id");

                            if (userId != null) {
                                ServerWebExchange modifiedExchange = addUserIdToRequest(exchange, userId);
                                // 수정된 exchange를 필터 체인으로 전달
                                return chain.filter(modifiedExchange);
                            }

                            // userId가 없으면 그대로 진행
                            return chain.filter(exchange);
                        });
            }
            // JWT가 필요하지 않은 경로는 그대로 통과
            return chain.filter(exchange);
        };
    }

    // JWT가 없는 경우 응답 처리
    private Mono<Void> handleMissingJwt(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        return exchange.getResponse().writeWith(
                Mono.just(
                        exchange.getResponse()
                                .bufferFactory()
                                .wrap("{\"error\" : \"토큰 정보 없음\"}".getBytes())
                )
        );
    }

    // JWT 검증 및 사용자 ID 확인
    private Mono<ResponseEntity<String>> verifyJwtAndGetUserId(String requestJwt) {
        return webClient.get()
                .uri("http://localhost:9000/user/getUserId")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + requestJwt)
                .retrieve()
                .toEntity(String.class);
    }

    private ServerWebExchange addUserIdToRequest(ServerWebExchange exchange, String userId) {
        return exchange.mutate()
                .request(r -> r.headers(headers -> headers.add("X-User-Id", userId))) // 최종 서비스에 사용자 ID 전달
                .build();
    }

    public static class Config {

    }
}
