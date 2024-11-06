package com.earlybird.orderservice.OrderItem.dto;

import com.earlybird.orderservice.OrderItem.entity.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class OrderItemResponse {
    private Long productId;
    private int quantity;
    private BigDecimal price;
}
