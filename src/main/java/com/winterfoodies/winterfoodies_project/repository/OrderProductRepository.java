package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.entity.FavoriteStore;
import com.winterfoodies.winterfoodies_project.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    // 특정가게 인기메뉴 가져오기
    @Query("SELECT op FROM OrderProduct op JOIN op.order o WHERE o.store.id = :storeId GROUP BY op.product.id ORDER BY SUM(op.product.id) DESC")
    public List<OrderProduct> findByStoreId(Long storeId);

    public List<OrderProduct> findByOrderId(Long orderId);


}