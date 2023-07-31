package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.cart.CartDto;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface CartService {
    // 장바구니에 상품 추가
    ProductDto addProductToCart(ProductDto inProductDto, HttpServletRequest request, HttpServletResponse response);

    // 장바구니 상품 목록 조회
    List<CartProductDto> getCartProduct(HttpServletRequest request);

    // 장바구니 특정 상품 삭제
    CartProductDto removeProductFromCart(@RequestParam Long productId, HttpServletRequest request, HttpServletResponse response);

    // 장바구니 초기화
    CartProductDto clearCart(HttpServletResponse response);

    // 주문하기 페이지
    OrderResponseDto getOrderConfirmPage(OrderRequestDto orderRequestDto, HttpServletRequest request);
}
