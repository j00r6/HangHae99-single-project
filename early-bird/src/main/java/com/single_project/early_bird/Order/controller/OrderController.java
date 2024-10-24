package com.single_project.early_bird.Order.controller;


import com.single_project.early_bird.Order.dto.OrderRequest;
import com.single_project.early_bird.OrderItem.dto.OrderItemRequest;
import com.single_project.early_bird.Order.service.OrderService;
import com.single_project.early_bird.Security.resolver.LoginUserId;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity orderProcess (@LoginUserId Long userId, @RequestBody OrderRequest request) {
        orderService.orderProcess(userId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("주문이 완료됐습니다");
    }
}
