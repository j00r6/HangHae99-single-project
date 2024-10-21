package com.single_project.early_bird.Product.repository;

import com.single_project.early_bird.Product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>  {
    List<Product> getProducts (Long cursor, int pageSize);
}
