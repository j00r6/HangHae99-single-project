package com.earlybird.productservice.Product.service;

import com.earlybird.productservice.Global.exception.BadRequestException;
import com.earlybird.productservice.Product.dto.ProductRequestDto;
import com.earlybird.productservice.Product.entity.Product;
import com.earlybird.productservice.Product.mapper.ProductMapper;
import com.earlybird.productservice.Product.repository.ProductRepository;
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
    private final ProductMapper productMapper;

    public List<Product> getProductsAfterCursor(Long cursor, int pageSize) {
        Pageable pageable = PageRequest.of(0, pageSize, Sort.by(Sort.Order.asc("id")));
        return productRepository.findProductsAfterCursor(cursor, pageable);
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
        Product product = productMapper.RequestToEntity(request);
        productRepository.save(product);
    }

    public void decreaseStock(Product product, int orderQuantity) {
        if (product.getStockQuantity() < orderQuantity) {
            throw new BadRequestException("제품 수량이 부족합니다");
        }
        product.setStockQuantity(product.getStockQuantity() - orderQuantity);
        productRepository.save(product);
    }

    public void increaseStock(Long productId, int cancelledStock){
        Product product = findVerifyProduct(productId);
        product.setStockQuantity(product.getStockQuantity() + cancelledStock);

    }
}
