package com.winterfoodies.winterfoodies_project.repository;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    public List<CartProduct> findByCartId(Long cartId);
}
