package com.single_project.early_bird.User.repository;

import com.single_project.early_bird.User.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>  {
}
