package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.CartDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.Cart;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import com.winterfoodies.winterfoodies_project.entity.Product;
import com.winterfoodies.winterfoodies_project.repository.ProductRepository;
import com.winterfoodies.winterfoodies_project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final UserService userService;
    private final ProductRepository productRepository;

    // 장바구니에 상품 추가 API
    @GetMapping("/items")
    public String addProductToCart(@RequestParam Long productId, @RequestParam Long quantity,HttpServletRequest request, HttpServletResponse response) {
        return userService.addProductToCart(productId, quantity, request, response);
    }

    // 장바구니에 상품 조회 API
    @GetMapping("/itemsList")
    public List<CartDto> getCartProducts(HttpServletRequest request) {
        return userService.getCartProduct(request);
    }


    // 장바구니 특정 상품 삭제
    @GetMapping("/remove")
    public String removeProduct(@RequestParam Long productId, HttpServletRequest request, HttpServletResponse response){
       return userService.removeProductFromCart(productId, request, response);
    }

    // 장바구니 초기화
    @GetMapping("/clear")
    public String clearCart(HttpServletResponse response) {
        return userService.clearCart(response);
    }

    // 주문완료 페이지
    @PostMapping("/items/confirm")
    public OrderResponseDto getOrderConfirmPage(@RequestBody OrderRequestDto orderRequestDto, HttpServletRequest request){
        return userService.getOrderConfirmPage(orderRequestDto, request);
    }
}
