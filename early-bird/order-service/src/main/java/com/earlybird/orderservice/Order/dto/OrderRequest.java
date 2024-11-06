package com.earlybird.orderservice.Order.dto;

import com.earlybird.orderservice.OrderItem.dto.OrderItemRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemResponse{
        private List<OrderItemRequest> cart;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class fromProduct {
        private Long productId;
        private String name;
        private BigDecimal price;
        private String description;
        private int stockQuantity;
    }
}
