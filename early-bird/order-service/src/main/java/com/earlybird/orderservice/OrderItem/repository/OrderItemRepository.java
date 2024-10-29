package com.earlybird.orderservice.OrderItem.repository;

import com.earlybird.orderservice.OrderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>  {
    Long findProductIdByOrderId(Long orderId);
}
