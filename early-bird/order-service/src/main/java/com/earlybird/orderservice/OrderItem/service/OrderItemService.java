package com.earlybird.orderservice.OrderItem.service;

import com.earlybird.orderservice.Order.entity.Order;
import com.earlybird.orderservice.OrderItem.entity.OrderItem;
import com.earlybird.orderservice.OrderItem.repository.OrderItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public void createOrderItem(Long productId, int quantity, BigDecimal price, Order order) {
        OrderItem orderItem = OrderItem.builder()
                .productId(productId)
                .quantity(quantity)
                .price(price)
                .order(order)
                .build();
        orderItemRepository.save(orderItem);
    }

    public Long getProductId(Long orderId){
        return orderItemRepository.findProductIdByOrderId(orderId);
    }
}
