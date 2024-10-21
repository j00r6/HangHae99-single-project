package com.single_project.early_bird.Product.controller;

import com.single_project.early_bird.Product.dto.ProductRequestDto;
import com.single_project.early_bird.Product.dto.ProductResponseDto;
import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @PostMapping("/register")
    public ResponseEntity<String> registerProduct(@RequestBody ProductRequestDto request) {
        productService.registerProduct(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("제품 등록 완료");
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProducts(@RequestParam(required = false, defaultValue = "0") Long cursor,
                                                     @RequestParam(required = false, defaultValue = "10") int pageSize) {
        List<Product> findProducts = productService.getProductsAfterCursor(cursor, pageSize);
        return ResponseEntity.ok(findProducts);
    }

    @GetMapping("/{product-id}")
    public ResponseEntity getProduct(@PathVariable("product-id") Long productId) {
        log.info("제품 아이디 확인 : " + productId);
        Product findProduct = productService.getProduct(productId);
        return ResponseEntity.ok(findProduct);
    }
}
