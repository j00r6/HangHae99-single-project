package com.earlybird.orderservice.Order.mapper;

import com.earlybird.orderservice.Order.dto.OrderResponse;
import com.earlybird.orderservice.Order.entity.Order;
import com.earlybird.orderservice.OrderItem.dto.OrderItemResponse;
import com.earlybird.orderservice.OrderItem.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponse EntityToResponse(Order order) {
        OrderResponse response = new OrderResponse();
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
