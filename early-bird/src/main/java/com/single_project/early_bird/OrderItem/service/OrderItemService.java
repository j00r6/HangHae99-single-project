package com.single_project.early_bird.OrderItem.service;

import com.single_project.early_bird.Order.entity.Order;
import com.single_project.early_bird.OrderItem.entity.OrderItem;
import com.single_project.early_bird.OrderItem.repository.OrderItemRepository;
import com.single_project.early_bird.Product.entity.Product;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    public void createOrderItem(Product product, int quantity, BigDecimal price, Order order) {
        OrderItem orderItem = OrderItem.builder()
                .product(product)
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
