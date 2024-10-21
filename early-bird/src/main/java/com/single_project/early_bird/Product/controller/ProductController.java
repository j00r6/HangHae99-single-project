package com.single_project.early_bird.Product.controller;

import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false, defaultValue = "0") Long cursor,
                                                     @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Product> findProducts = productService.getProductsAfterCursor(cursor, pageSize);
        return ResponseEntity.ok(findProducts);
    }

    @GetMapping
    public ResponseEntity<Product> getProduct(@RequestParam Long productId) {
        Product findProduct = productService.getProduct(productId);
        return ResponseEntity.ok(findProduct);
    }
}
