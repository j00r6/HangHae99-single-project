package com.single_project.early_bird.OrderItem.repository;

import com.single_project.early_bird.OrderItem.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>  {
}
