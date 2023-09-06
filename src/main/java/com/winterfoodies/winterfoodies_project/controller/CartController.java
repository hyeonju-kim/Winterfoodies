package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.dto.cart.CartResponseDto;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductDto;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductRequestDto;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductResponseDto;
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
import io.swagger.annotations.ApiOperation;
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
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final UserService userService;
    private final CartService cartService;
    private final ProductRepository productRepository;

    @PostMapping("/items") // [230726] Get -> Post로 변경
    @ApiOperation(value = "장바구니에 상품 추가")
    public CartProductResponseDto addProductToCart(@Valid @RequestBody ProductRequestDto productRequestDto, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws Exception{
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

        log.info("id={}" , productRequestDto.getProductId().toString());
        log.info("quantity={}" , productRequestDto.getQuantity().toString());
        log.info("request={}" , request);
        log.info("response={}" , response);

        CartProductDto cartProductDto = new CartProductDto(productRequestDto);

        CartProductDto cartProductDto1 = cartService.addProductToCart(cartProductDto, request, response);
        return cartProductDto1.convertToCartProductResponseDto();
    }

//    @ExceptionHandler(RequestException.class)
//    public ErrorBox requestExceptionHandler(RequestException requestException) {
//        return requestException.getErrorBox();
//    }

    // 장바구니에 상품 조회 API
//    @GetMapping("/itemsList")
//    @ApiOperation(value = "장바구니 상품 조회 - 쿠키에서 조회")
//    public List<CartProductResponseDto> getCartProducts(HttpServletRequest request) {
//        List<CartProductDto> cartProductDtoList = cartService.getCartProduct(request);
//        ArrayList<CartProductResponseDto> cartProductResponseDtoList = new ArrayList<>();
//        for (CartProductDto cartProductDto : cartProductDtoList) {
//            cartProductResponseDtoList.add(cartProductDto.convertToCartProductResponseDto());
//        }
//        return cartProductResponseDtoList;
//    }

    // 장바구니 상품 목록 조회 (DB에서)
    @GetMapping("/itemsListByDB")
    @ApiOperation(value = "장바구니 상품 조회 - DB에서 조회")
    public CartDto getCartProductByDB() {
        return cartService.getCartProductByDB();
    }

    // 장바구니에서 상품 수량 증가
    @PostMapping("/incQnt/{productId}")
    public CartDto increaseQuantity(@PathVariable Long productId) {
        return cartService.increaseQuantity(productId);

    }

    // 장바구니에서 상품 수량 감소
    @PostMapping("/decQnt/{productId}")
    public CartDto decreaseQuantity(@PathVariable Long productId) {
        return cartService.decreaseQuantity(productId);

    }

    // 장바구니 특정 상품 삭제
    @DeleteMapping  ("/{productId}") // 230731 Get -> Delete로 변경, @requestParam -> @PathVariable로 변경
    @ApiOperation(value = "장바구니 특정 상품 삭제")
    public CartProductResponseDto removeProduct(@PathVariable Long productId, HttpServletRequest request, HttpServletResponse response){
        CartProductDto cartProductDto = cartService.removeProductFromCart(productId, request, response);
        return cartProductDto.convertToCartProductResponseDto();
    }

    // 230831 주석처리
//    // 장바구니 초기화  -> 실제 앱에는 필요 없을듯.. 일단 빼자!
//    @PutMapping // 230731 Get -> Put 으로 변경
//    @ApiOperation(value = "장바구니 초기화")
//    public CartProductResponseDto clearCart(HttpServletResponse response) {
//        CartProductDto cartProductDto = cartService.clearCart(response);
//        return cartProductDto.convertToCartProductResponseDto();
//    }

//    // 주문완료 페이지
//    @PostMapping("/items/confirm")
//    @ApiOperation(value = "주문 완료 페이지 - 쿠키에서 조회")
//    public OrderResponseDto getOrderConfirmPage(@RequestBody OrderRequestDto orderRequestDto, HttpServletRequest request){
//        return cartService.getOrderConfirmPage(orderRequestDto, request);
//    }

    // 주문완료 페이지 (DB에서 조회)
    @PostMapping("/items/confirmByDB")
    @ApiOperation(value = "주문 완료 페이지 - DB에서 조회")
    public OrderResponseDto getOrderConfirmPageByDB(@RequestBody OrderRequestDto orderRequestDto){
        return cartService.getOrderConfirmPageByDB(orderRequestDto);
    }
}
