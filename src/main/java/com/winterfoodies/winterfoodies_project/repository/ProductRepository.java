package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}