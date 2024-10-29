package com.earlybird.productservice.Wishlist.repository;

import com.earlybird.productservice.Wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long>  {
    Wishlist findWishlistIdByProduct_ProductId (Long productId);
}
