package com.single_project.early_bird.Product.service;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProductsAfterCursor(Long cursor, int pageSize) {
        return productRepository.getProducts(cursor, pageSize);
    }

    public Product getProduct(Long productId) {
        return findVerifyProduct(productId);
    }

    private Product findVerifyProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        Product findProduct = product.orElseThrow(() -> new BadRequestException("Product not found"));
        return findProduct;
    }
}
