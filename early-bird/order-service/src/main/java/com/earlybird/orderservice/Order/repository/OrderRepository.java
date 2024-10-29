package com.earlybird.orderservice.Order.repository;

import com.earlybird.orderservice.Order.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>  {
//    @Query("SELECT o FROM Order o WHERE o.user.userId > :cursor")
    @Query("SELECT o FROM Order o WHERE o.user.userId = :userId AND o.orderId > :cursor ORDER BY o.createdAt DESC")
    List<Order> findOrdersByUserIdAndCursor(Long userId, @Param("cursor") Long cursor, Pageable pageable);

    Order findOrderByUserIdAndOrderId(Long userId, Long orderId);
}