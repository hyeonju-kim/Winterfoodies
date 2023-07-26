package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.dto.product.ProductDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.exception.RequestException;
import com.winterfoodies.winterfoodies_project.dto.order.OrderRequestDto;
import com.winterfoodies.winterfoodies_project.dto.order.OrderResponseDto;
import com.winterfoodies.winterfoodies_project.dto.product.ProductRequestDto;
import com.winterfoodies.winterfoodies_project.dto.cart.CartDto;
import com.winterfoodies.winterfoodies_project.repository.ProductRepository;
import com.winterfoodies.winterfoodies_project.service.CartService;
import com.winterfoodies.winterfoodies_project.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final UserService userService;
    private final CartService cartService;
    private final ProductRepository productRepository;

//    // jwt 토큰으로 현재 인증된 사용자의 Authentication 객체에서 이름 가져오기
//    public String getUsernameFromAuthentication() {
//        String username = null;
//        if (SecurityContextHolder.getContext().getAuthentication() != null) {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            // 인증된 사용자의 이름 가져오기
//            username = authentication.getName();
//        }
//        return username;
//    }
//
//    // 인증된 사용자의 id 가져오기
//    public Long getUserId() {
//        User foundUser = userRepository.findByUsername(getUsernameFromAuthentication());
//        return foundUser.getId();
//    }

    // 장바구니에 상품 추가 API
    // 230707 valid 추가 !!!!!
    @PostMapping("/items") // [230726] TODO 앱개발자 전달사항 :  Get -> Post로 변경
    public ProductResponseDto addProductToCart(@Valid @RequestBody ProductRequestDto productRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if (bindingResult.hasErrors()) {
            ObjectError err = bindingResult.getAllErrors().get(0);
            String message = err.getDefaultMessage();
            String code = err.getCode();
            LocalDateTime now = LocalDateTime.now();
            String dateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            ErrorBox errorBox = new ErrorBox();
            errorBox.setCause(code);
            errorBox.setMessage("[ 고객 id ] : " + userService.getUserId() + " | [ 에러 유형 ] : "+ code + " | [ 에러 시간 ] : " + dateTime + " | [ 에러메시지 ] : "+ message); //이 형태 그대로 사용해야함. 길이 맞춰놓음 스케줄러에서 사용

            log.error(errorBox.getMessage());
            throw new RequestException(errorBox);
        }
        log.info("id={}" , productRequestDto.getId().toString());
        log.info("quantity={}" , productRequestDto.getQuantity().toString());
        log.info("request={}" , request);
        log.info("response={}" , response);

        ProductDto productDto = new ProductDto(productRequestDto);

        ProductDto productDto1 = cartService.addProductToCart(productDto, request, response);
        return productDto1.convertToProductResponseDto();
    }

    @ExceptionHandler(RequestException.class)
    public ErrorBox requestExceptionHandler(RequestException requestException) {
        return requestException.getErrorBox();
    }


    // 장바구니에 상품 조회 API
    @GetMapping("/itemsList")
    public List<CartDto> getCartProducts(HttpServletRequest request) {
        return cartService.getCartProduct(request);
    }


    // 장바구니 특정 상품 삭제
    @GetMapping("/remove") // Delete로변경()
    public String removeProduct(@RequestParam Long productId, HttpServletRequest request, HttpServletResponse response){
       return cartService.removeProductFromCart(productId, request, response);
    }

    // 장바구니 초기화
    @GetMapping("/clear")
    public String clearCart(HttpServletResponse response) {
        return cartService.clearCart(response);
    }

    // 주문완료 페이지
    @PostMapping("/items/confirm")
    public OrderResponseDto getOrderConfirmPage(@RequestBody OrderRequestDto orderRequestDto, HttpServletRequest request){
        return cartService.getOrderConfirmPage(orderRequestDto, request);
    }
}
