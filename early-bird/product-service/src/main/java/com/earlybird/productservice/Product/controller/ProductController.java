package com.earlybird.productservice.Product.controller;

import com.earlybird.productservice.Product.dto.ProductRequestDto;
import com.earlybird.productservice.Product.dto.ProductResponseDto;
import com.earlybird.productservice.Product.entity.Product;
import com.earlybird.productservice.Product.mapper.ProductMapper;
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
    private final ProductMapper productMapper;

    /**
     * 제품 등록의 경우 일반 회원은 진행하지 못하고
     * 판매자의 경우만 가능하므로, 회원가입시 인증정보에서 roles 를 가져와 검증
     *
     * @param roles
     * @param request
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerProduct(@RequestAttribute("roles") String roles,
                                                  @RequestBody ProductRequestDto request) {
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

    /**
     * 제품 상세보기(단일 조회)의 경우 비회원도 진행 가능
     *
     * @param productId
     * @return
     */
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto.toOrder> getProduct(@PathVariable("productId") Long productId) {
        log.info("제품 아이디 확인 : " + productId);
        ProductResponseDto.toOrder findProduct = productService.getProduct(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(findProduct);
    }
}
