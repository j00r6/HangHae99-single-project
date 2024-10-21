package com.single_project.early_bird.User.repository;

import com.single_project.early_bird.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>  {
    boolean existsByEmail(String email);
    boolean findVerifiedByEmail(String email);
    Optional<User> findByEmail(String email);
}
