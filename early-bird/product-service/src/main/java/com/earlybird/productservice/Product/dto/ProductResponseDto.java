package com.earlybird.productservice.Product.dto;

import com.earlybird.productservice.Product.entity.Product;
import lombok.*;

import java.math.BigDecimal;

public class ProductResponseDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class toOrder {
        private Long productId;
        private String name;
        private BigDecimal price;
        private String description;
        private int stockQuantity;
    }

}
