package com.earlybird.productservice.Wishlist.controller;

import com.earlybird.productservice.Wishlist.entity.Wishlist;
import com.earlybird.productservice.Wishlist.service.WishlistService;
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
    public ResponseEntity<String> Favorite(@RequestHeader("X-User-Id") Long userId,
                                           @PathVariable Long productId) {
        wishlistService.addFavorite(userId, productId);
        Wishlist findFavorite = wishlistService.getFavorite(productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("찜 설정");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeFavorite(@RequestHeader("X-User-Id") Long userId,
                                                 @PathVariable Long productId) {
        wishlistService.removeFavorite(userId, productId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body("찜 해제");
    }
}
