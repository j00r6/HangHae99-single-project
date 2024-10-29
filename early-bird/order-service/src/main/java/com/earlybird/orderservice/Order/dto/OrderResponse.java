package com.earlybird.orderservice.Order.dto;

import com.single_project.early_bird.Order.entity.Order;
import com.single_project.early_bird.Order.entity.OrderStatus;
import com.single_project.early_bird.OrderItem.dto.OrderItemResponse;;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private OrderStatus status;
    private List<OrderItemResponse> orders;



    public static OrderResponse OrderEntityToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getOrderId());
        response.setUserId(order.getUser().getUserId());
        response.setStatus(order.getStatus());
        response.setOrders(OrderItemResponse.OrderItemToItemResponse(order.getOrderItems()));
        return response;
    }
}
