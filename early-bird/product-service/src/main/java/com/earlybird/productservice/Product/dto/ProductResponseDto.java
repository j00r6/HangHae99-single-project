package com.earlybird.productservice.Product.dto;

import com.earlybird.productservice.Product.entity.Product;
import lombok.*;

import java.math.BigDecimal;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long productId;
    private String name;
    private BigDecimal price;
    private String description;
    private int stockQuantity;
}
