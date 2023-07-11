package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.RequestException;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductRequestDto;
import com.winterfoodies.winterfoodies_project.dto.user.CartDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.Cart;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import com.winterfoodies.winterfoodies_project.entity.Product;
import com.winterfoodies.winterfoodies_project.repository.ProductRepository;
import com.winterfoodies.winterfoodies_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final UserService userService;
    private final ProductRepository productRepository;

    // 장바구니에 상품 추가 API
    // valid 추가 !!!!!
    @GetMapping("/items")
    public String addProductToCart(@Valid ProductRequestDto productRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (bindingResult.hasErrors()) {
            ObjectError err = bindingResult.getAllErrors().get(0);
            String message = err.getDefaultMessage();
            String code = err.getCode();
            ErrorBox errorBox = new ErrorBox();
            errorBox.setCause(code);
            errorBox.setMessage("[고객id] : " + productRequestDto.getId() + "[에러메시지] : "+ message);
            log.error(message);
            throw new RequestException(errorBox);
        }
        return "장바구니에 추가되었습니다";
//        return userService.addProductToCart(productRequestDto.getId(), productRequestDto.getQuantity(), request, response);
    }

    @ResponseBody
    @ExceptionHandler(RequestException.class)
    public ErrorBox requestExceptionHandler(RequestException requestException) {
        return requestException.getErrorBox();
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
