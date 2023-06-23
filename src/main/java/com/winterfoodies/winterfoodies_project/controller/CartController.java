package com.winterfoodies.winterfoodies_project.controller;

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
    @PostMapping("/{cartId}/items")
    public UserResponseDto addProductToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        return userService.addProductToCart(cartId, productId, quantity);
    }

//  위의 코드를 이렇게 써도 된다 !
//    @PostMapping("/{cartId}/items")
//    public ResponseEntity<String> addProductToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
//        userService.addProductToCart(cartId, productId, quantity);
//        return ResponseEntity.ok("장바구니에 추가되었습니다!");
//    }

//  위의 코드를 이렇게 써도 된다 !
//    @PostMapping("/{cartId}/items")
//    public void addProductToCart(@PathVariable Long cartId, @RequestParam Long productId, @RequestParam int quantity, HttpServletResponse response) {
//        userService.addProductToCart(cartId, productId, quantity);
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
//        try {
//            response.getWriter().write("Product added to cart");
//        } catch (IOException e) {
//            // 예외 처리
//        }
//    }


    // 장바구니 상품 목록 조회 API
//    @GetMapping("/{cartId}/items")
//    public ResponseEntity<List<CartProduct>> getCartItems(@PathVariable Long cartId) {
//        List<CartProduct> cartProducts = userService.getCartItems(cartId);
//        return ResponseEntity.ok(cartProducts);
//    }
    @GetMapping("/{cartId}/items")
    public List<CartProduct> getCartProducts(@PathVariable Long cartId) {
//        return userService.getCartProduct(cartId);
        Cart cart = new Cart();
        cart.setId(1L);
        Product product = new Product("호빵", 5L);
        product.setId(1L);
        product.setPrice(4000L);
        CartProduct cartProduct = new CartProduct(cart, product, 5);
        List<CartProduct> cartProductList = new ArrayList<>();
        cartProductList.add(cartProduct);
        return cartProductList;

    }

}
