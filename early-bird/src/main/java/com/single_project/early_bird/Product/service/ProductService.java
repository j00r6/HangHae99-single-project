package com.single_project.early_bird.Product.service;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.Product.dto.ProductRequestDto;
import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProductsAfterCursor(Long cursor, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.asc("id")));
        return productRepository.findPostsAfterCursor(cursor, pageable);
    }

    public Product getProduct(Long productId) {
        return findVerifyProduct(productId);
    }

    public Product findVerifyProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        Product findProduct = product.orElseThrow(() -> new BadRequestException("재고가 없습니다"));
        return findProduct;
    }

    public void registerProduct(ProductRequestDto request) {
        Product product = request.RequestDtoToEntity(request);
        productRepository.save(product);
    }
}
