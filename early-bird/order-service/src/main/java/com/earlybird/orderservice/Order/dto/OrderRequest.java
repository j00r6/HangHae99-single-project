package com.earlybird.orderservice.Order.dto;

import com.earlybird.orderservice.OrderItem.dto.OrderItemRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class OrderRequest {
    private List<OrderItemRequest> cart;
}
