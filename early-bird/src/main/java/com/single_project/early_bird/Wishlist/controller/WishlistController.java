package com.single_project.early_bird.Wishlist.controller;

import com.single_project.early_bird.Wishlist.entity.Wishlist;
import com.single_project.early_bird.Wishlist.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping("/{productId}")
    public ResponseEntity<String> likeProduct(@PathVariable Long productId) {
        wishlistService.addFavorite(productId);
        Wishlist findFavorite = wishlistService.getFavorite(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("찜 설정");
    }
}
