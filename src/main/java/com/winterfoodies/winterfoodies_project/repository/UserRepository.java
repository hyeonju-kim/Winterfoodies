package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    User findByUsername(String username);

    User findByUsernameAndEmail(String username, String email);


    List<User> findAllByUsername(String username);

}
