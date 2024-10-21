package com.single_project.early_bird.Product.dto;

import com.single_project.early_bird.Product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String description;
    private int stockQuantity;
}
