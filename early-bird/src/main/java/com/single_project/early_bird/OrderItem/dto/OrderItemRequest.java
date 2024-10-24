package com.single_project.early_bird.OrderItem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemRequest {
    private Long productId;
    private int quantity;
    private BigDecimal price;
}
