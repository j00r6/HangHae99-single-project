package com.earlybird.orderservice.OrderItem.dto;

import com.single_project.early_bird.OrderItem.entity.OrderItem;
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

    // Getter, Setter

    // 변환 메서드 추가
    public static List<OrderItemResponse> OrderItemToItemResponse (List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    OrderItemResponse response = new OrderItemResponse();
                    response.setProductId(item.getProduct().getProductId());
                    response.setQuantity(item.getQuantity());
                    response.setPrice(item.getPrice());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
