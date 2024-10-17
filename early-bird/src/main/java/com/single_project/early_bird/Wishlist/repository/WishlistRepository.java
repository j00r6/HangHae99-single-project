package com.single_project.early_bird.Wishlist.repository;

import com.single_project.early_bird.Wishlist.entity.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long>  {
}
