package com.earlybird.productservice.Wishlist.service;

import com.earlybird.productservice.Product.entity.Product;
import com.earlybird.productservice.Product.service.ProductService;
import com.earlybird.productservice.Wishlist.entity.Wishlist;
import com.earlybird.productservice.Wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final UserService userService;

    public void addFavorite(Long userId, Long productId) {
        Product product = productService.getProduct(productId);
        userService.findVerifyUser(userId);

        Wishlist wishlist = new Wishlist();
        wishlist.setProduct(product);
        wishlist.setUserId(userId);
        wishlistRepository.save(wishlist);
    }

    public void removeFavorite(Long userId, Long productId) {
        getFavorite(productId);
        User user = userService.findVerifyUser(userId);
        Wishlist wishlistId = wishlistRepository.findWishlistIdByProduct_ProductId(productId);
        wishlistRepository.delete(wishlistId);
    }

    public Wishlist getFavorite(Long productId) {
        return findFavorite(productId);
    }

    private Wishlist findFavorite(Long productId) {
        Optional<Wishlist> wishlist = wishlistRepository.findById(productId);
        Wishlist findFavorite = wishlist.orElseThrow(() -> new BadRequestException("제품 정보가 존재하지 않습니다"));
        return findFavorite;
    }
}
