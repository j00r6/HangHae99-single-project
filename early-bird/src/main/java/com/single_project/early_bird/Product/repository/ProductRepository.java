package com.single_project.early_bird.Product.repository;

import com.single_project.early_bird.Product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>  {
}
