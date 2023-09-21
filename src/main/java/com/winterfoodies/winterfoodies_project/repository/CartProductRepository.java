package com.winterfoodies.winterfoodies_project.repository;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductResponseDto;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
     List<CartProduct> findByCartId(double cartId);

    @Transactional
     void deleteByProductId(double productId);


    @Transactional
    List<CartProduct> findByUserId(double userId);

     List<CartProduct> findByStoreId(double storeId);

    CartProduct findByProductId(double productId);



}
