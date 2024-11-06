package com.earlybird.productservice.Product.mapper;

import com.earlybird.productservice.Product.dto.ProductRequestDto;
import com.earlybird.productservice.Product.dto.ProductResponseDto;
import com.earlybird.productservice.Product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product RequestToEntity(ProductRequestDto request) {
        return Product.builder()
                .name(request.getName())
                .price(request.getPrice())
                .description(request.getDescription())
                .stockQuantity(request.getStockQuantity())
                .build();
    }

    public ProductResponseDto.toOrder EntityToRequestDto(Product product) {
        return ProductResponseDto.toOrder.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .stockQuantity(product.getStockQuantity())
                .build();
    }
}
