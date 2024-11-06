package com.earlybird.productservice.Product.dto;

import com.earlybird.productservice.Product.entity.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private BigDecimal price;
    private String description;
    private int stockQuantity;


}
