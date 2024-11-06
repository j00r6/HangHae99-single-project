package com.earlybird.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class ExceptionFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange)
                .onErrorResume(e -> {
                    // 상태 코드 기본 설정 (500 Internal Server Error)
                    HttpStatusCode statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

                    // 에러 메시지 기본 설정
                    String errorMessage = "An unexpected error occurred";

                    // ResponseStatusException인 경우
                    if (e instanceof ResponseStatusException) {
                        ResponseStatusException ex = (ResponseStatusException) e;
                        statusCode = ex.getStatusCode();  // 상태 코드 가져오기
                        errorMessage = ex.getReason();  // 서비스에서 반환한 에러 메시지 가져오기
                    } else if (e instanceof RuntimeException) {
                        // RuntimeException인 경우 기본 메시지 설정
                        statusCode = HttpStatus.BAD_REQUEST;
                        errorMessage = e.getMessage();
                    }

                    // 에러 상태 코드일 때만 처리 (4xx, 5xx 범위)
                    if (statusCode.isError()) {
                        // 서비스에서 반환한 에러 메시지가 JSON 형식이라면 그대로 사용
                        // 서비스에서 에러 메시지가 JSON 형식으로 넘어온다고 가정
                        String errorResponse = errorMessage;  // 서비스에서 반환된 JSON 에러 메시지 그대로 사용

                        // 상태 코드와 함께 클라이언트에 응답
                        exchange.getResponse().setStatusCode(statusCode);
                        exchange.getResponse().getHeaders().add("Content-Type", "application/json");

                        // 에러 메시지 그대로 클라이언트에 전송
                        return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(errorResponse.getBytes())));
                    }

                    // 에러가 아닌 경우는 그냥 넘어가게 처리
                    return Mono.empty();
                });

    }
}
