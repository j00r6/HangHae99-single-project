package com.earlybird.productservice.Product.controller;

import com.earlybird.productservice.Product.dto.ProductRequestDto;
import com.earlybird.productservice.Product.dto.ProductResponseDto;
import com.earlybird.productservice.Product.entity.Product;
import com.earlybird.productservice.Product.service.ProductService;
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

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findProducts);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable("productId") Long productId) {
        log.info("제품 아이디 확인 : " + productId);
        Product findProduct = productService.getProduct(productId);
        ProductResponseDto response = findProduct.EntityToRequestDto(findProduct);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
