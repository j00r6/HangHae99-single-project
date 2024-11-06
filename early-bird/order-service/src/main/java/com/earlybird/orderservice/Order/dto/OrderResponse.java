package com.earlybird.orderservice.Order.dto;

import com.earlybird.orderservice.Order.entity.Order;
import com.earlybird.orderservice.Order.entity.OrderStatus;
import com.earlybird.orderservice.OrderItem.dto.OrderItemResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private OrderStatus status;
    private List<OrderItemResponse> orders;
}
