package com.earlybird.orderservice.Order.controller;

import com.earlybird.orderservice.Order.dto.OrderRequest;
import com.earlybird.orderservice.Order.dto.OrderResponse;
import com.earlybird.orderservice.Order.service.OrderService;
import com.earlybird.userservice.Security.resolver.LoginUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<String> orderProcess (@LoginUserId Long userId, @RequestBody OrderRequest request) {
        orderService.createOrder(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("주문이 완료됐습니다");
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getOrders (@LoginUserId Long userId,
                                                          @RequestParam(required = false, defaultValue = "0") Long cursor,
                                                          @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<OrderResponse> findOrderList = orderService.getOrdersAfterCursor(userId, cursor, pageSize);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findOrderList);
    }

    @PutMapping("/cancel/{orderId}")
    public ResponseEntity<String> cancelOrder (@LoginUserId Long userId, @PathVariable Long orderId) {
        orderService.cancelOrder(userId, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("주문 취소 완료");
    }

    @PutMapping("/refund/{orderId}")
    public ResponseEntity<String> refundOrder (@LoginUserId Long userId, @PathVariable Long orderId) {
        orderService.refundOrder(userId, orderId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("제품 환불 완료");
    }
}
