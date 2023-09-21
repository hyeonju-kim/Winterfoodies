package com.winterfoodies.winterfoodies_project.repository;

import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.entity.FavoriteStore;
import com.winterfoodies.winterfoodies_project.entity.OrderProduct;
import com.winterfoodies.winterfoodies_project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    // 특정가게 인기메뉴 가져오기
    @Query("SELECT op FROM OrderProduct op JOIN op.order o WHERE o.store.id = :storeId GROUP BY op.product.id ORDER BY SUM(op.product.id) DESC")
    List<OrderProduct> findByStoreId(Long storeId);

    List<OrderProduct> findByOrderId(Long orderId);

    // 지금까지 판매된 상품 중에 가장 많이 팔린 인기 상품 5개 노출
    @Query("SELECT op.product FROM OrderProduct op GROUP BY op.product.id ORDER BY SUM(op.quantity) DESC")
    List<Product> findTop5PopularProducts();

    // 특정가게에서 지금까지 판매된 상품 중에 가장 많이 팔린 상품 5개 노출
    @Query("SELECT op.product FROM OrderProduct op WHERE op.product.storeDetail.id = :storeId GROUP BY op.product.id ORDER BY SUM(op.quantity) DESC")
    List<Product> findTop5PopularProductsByStoreId(@Param("storeId") Long storeId);


}