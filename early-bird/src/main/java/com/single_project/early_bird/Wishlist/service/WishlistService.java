package com.single_project.early_bird.Wishlist.service;

import com.single_project.early_bird.Global.exception.BadRequestException;
import com.single_project.early_bird.Product.entity.Product;
import com.single_project.early_bird.Product.service.ProductService;
import com.single_project.early_bird.User.entity.User;
import com.single_project.early_bird.User.service.UserService;
import com.single_project.early_bird.Wishlist.entity.Wishlist;
import com.single_project.early_bird.Wishlist.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final UserService userService;

    public void addFavorite(Long productId) {
        Product product = productService.getProduct(productId);
//        User user = userService.findVerifyUser(userId);

        Wishlist wishlist = new Wishlist();
        wishlist.setProduct(product);
//        wishlist.setUser(user);

        wishlistRepository.save(wishlist);
    }

    public void removeFavorite(Long productId) {}

    public Wishlist getFavorite(Long productId) {
        return findFavorite(productId);
    }

    private Wishlist findFavorite(Long productId) {
        Optional<Wishlist> wishlist = wishlistRepository.findById(productId);
        Wishlist findFavorite = wishlist.orElseThrow(() -> new BadRequestException("Product not found"));
        return findFavorite;
    }
}
