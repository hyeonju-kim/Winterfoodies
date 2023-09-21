package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.entity.FavoriteStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteStoreRepository extends JpaRepository<FavoriteStore, Long> {
    List<FavoriteStore> findByUserId(double userId);

    Optional<FavoriteStore> findByUserIdAndStoreId(double userId, double storeId);
}
