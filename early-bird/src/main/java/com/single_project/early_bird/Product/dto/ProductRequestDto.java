package com.single_project.early_bird.Product.dto;

import com.single_project.early_bird.Product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private BigDecimal price;
    private String description;
    private int stockQuantity;

    public Product RequestDtoToEntity(ProductRequestDto request) {
        return Product.builder()
                .name(this.name)
                .price(this.price)
                .description(this.description)
                .stockQuantity(this.stockQuantity)
                .build();
    }
}
