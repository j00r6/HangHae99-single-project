package com.earlybird.orderservice.Order.mapper;

import com.earlybird.orderservice.Order.dto.OrderRequest;
import com.earlybird.orderservice.Order.dto.OrderResponse;
import com.earlybird.orderservice.Order.entity.Order;
import com.earlybird.orderservice.OrderItem.dto.OrderItemResponse;
import com.earlybird.orderservice.OrderItem.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public static OrderResponse.toOrder EntityToResponse(Order order) {
        OrderResponse.toOrder response = new OrderResponse.toOrder();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUserId());
        response.setStatus(order.getStatus());
        response.setOrders(OrderItemToItemResponse(order.getOrderItems()));
        return response;
    }

    public static List<OrderItemResponse> OrderItemToItemResponse (List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> {
                    com.earlybird.orderservice.OrderItem.dto.OrderItemResponse response = new com.earlybird.orderservice.OrderItem.dto.OrderItemResponse();
                    response.setProductId(item.getProductId());
                    response.setQuantity(item.getQuantity());
                    response.setPrice(item.getPrice());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
