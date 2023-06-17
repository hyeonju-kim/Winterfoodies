package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
