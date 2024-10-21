package com.single_project.early_bird.Product.repository;

import com.single_project.early_bird.Product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>  {
    @Query("SELECT p FROM Product p WHERE p.productId > :cursor ORDER BY p.productId ASC")
    List<Product> findPostsAfterCursor(@Param("cursor") Long cursor, Pageable pageable);
}
