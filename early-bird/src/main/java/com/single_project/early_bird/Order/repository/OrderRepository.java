package com.single_project.early_bird.Order.repository;

import com.single_project.early_bird.Order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>  {
}
