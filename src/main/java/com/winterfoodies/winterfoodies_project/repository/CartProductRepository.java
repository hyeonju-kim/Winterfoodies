package com.winterfoodies.winterfoodies_project.repository;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductResponseDto;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
     List<CartProduct> findByCartId(Long cartId);

    @Transactional
     void deleteByProductId(Long productId);


    @Transactional
    List<CartProduct> findByUserId(Long userId);

     List<CartProduct> findByStoreId(Long storeId);

    CartProduct findByProductId(Long productId);



}
