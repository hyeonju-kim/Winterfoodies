package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByUserId(double userId);

    List<Review> findByStoreId(double storeId);
    void deleteById(double reviewId);

    double countByStoreId(double storeId);

}