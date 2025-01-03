package com.earlybird.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
@Slf4j
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    private final WebClient webClient = WebClient.create();

    public AuthFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            HttpHeaders headers = request.getHeaders();

            // 토큰이 없는 경우 처리
            if (!headers.containsKey(HttpHeaders.AUTHORIZATION)) {
                log.info("토큰이 없어용");
                return onError(exchange, "토큰 정보가 없습니다", HttpStatus.UNAUTHORIZED);
            }

            String token = headers.get(HttpHeaders.AUTHORIZATION).get(0);

            String userId = String.valueOf(toAuthServer(exchange, chain, token));

            // JWT 검증 메서드 호출
            return toAuthServer(exchange, chain, token);
        };
    }

    public Mono<Void> toAuthServer(ServerWebExchange exchange, GatewayFilterChain chain, String token) {
        return validateToken(token)
                .flatMap(userId -> addUserIdToHeader(exchange, userId))
                .flatMap(chain::filter)
                .onErrorResume(error -> handleError(exchange, error));
    }

    //TODO : 인증 서버에서 에러 반환 시 최종 서비스로 라우팅 중단하기
    // JWT 검증 요청 처리
    private Mono<String> validateToken(String token) {
        return webClient.get()
                .uri("http://localhost:9000/auth/validate")
                .header(HttpHeaders.AUTHORIZATION, token)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        // 성공 서비스에서 전달된 메세지 그대로 활용하여 응답
                        return response.bodyToMono(String.class);
                    } else {
                        // CASE#01 실패 : 서비스에서 전달된 메세지 그대로 활용하여 응답
                        return response.bodyToMono(String.class);
                        // CASE#02 실패 : 서비스에서 오류코드만 받아서 gateway에서 예외 발생
//                        return response.createException()
//                                .flatMap(Mono::error);
                    }
                });


    }

    // 헤더에 X-User-Id 추가
    private Mono<ServerWebExchange> addUserIdToHeader(ServerWebExchange exchange, String userId) {
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-User-Id", userId)
                        .build())
                .build();
        return Mono.just(modifiedExchange);
    }

    // 오류 처리
    private Mono<Void> handleError(ServerWebExchange exchange, Throwable error) {
        return onError(exchange, exchange.getResponse().toString(), HttpStatus.FORBIDDEN);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String errorMessage, HttpStatus status) {
        exchange.getResponse().setStatusCode(status);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String errorResponse = String.format("{\"error\": \"%s\"}", errorMessage);
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(errorResponse.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

    // Config 클래스: 필터 구성에 사용되는 설정값 정의
    public static class Config {
        // 필요한 설정값을 추가할 수 있습니다. (현재는 필요 없음)
    }
}