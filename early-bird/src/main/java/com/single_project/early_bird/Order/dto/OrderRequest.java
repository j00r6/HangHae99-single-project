package com.single_project.early_bird.Order.dto;

import com.single_project.early_bird.OrderItem.dto.OrderItemRequest;
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
