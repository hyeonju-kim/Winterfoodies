package com.winterfoodies.winterfoodies_project.dto.cartProduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.ErrorBox;
import com.winterfoodies.winterfoodies_project.dto.product.ProductRequestDto;
import com.winterfoodies.winterfoodies_project.entity.Cart;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import com.winterfoodies.winterfoodies_project.entity.Store;
import com.winterfoodies.winterfoodies_project.entity.User;
import com.winterfoodies.winterfoodies_project.exception.RequestException;
import com.winterfoodies.winterfoodies_project.exception.UserException;
import com.winterfoodies.winterfoodies_project.repository.CartRepository;
import com.winterfoodies.winterfoodies_project.repository.StoreRepository;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import com.winterfoodies.winterfoodies_project.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import java.util.Optional;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class CartProductDto {
    private double id;
    private double productId;
    private String productName;
    private double quantity;
    private double pricePerProduct;
    private double subTotalPrice; // 장바구니의 각 상품별 합계
    private double totalPrice;
    private String message;
    private double storeId;
    private String storeName;
    private String estimatedCookingTime;
    private double userId;



    // 기본 생성자
    public CartProductDto() {

    }

    // 1. requestDto -> Dto
    public CartProductDto(CartProductRequestDto requestDto, double userId) {
        this.productId = requestDto.getProductId();
        this.quantity = requestDto.getQuantity();
        this.subTotalPrice = requestDto.getSubTotalPrice();
        this.totalPrice = requestDto.getTotalPrice();
        this.userId = userId;
    }

    public CartProductDto(ProductRequestDto requestDto) {
        this.productId = requestDto.getProductId();
        this.quantity = requestDto.getQuantity();
        this.storeId = requestDto.getStoreId();
    }

    // 2. entity -> Dto
    public CartProductDto(CartProduct cartProduct, double userId) {
        this.id = cartProduct.getId();
        this.productId = cartProduct.getProduct().getId();
        this.productName = cartProduct.getProduct().getName();
        this.quantity = cartProduct.getQuantity();
        this.subTotalPrice = cartProduct.getSubTotalPrice();
        this.pricePerProduct = cartProduct.getProduct().getPrice();
        this.userId = userId;
    }

    // 3. Dto -> responseDto
    public CartProductResponseDto convertToCartProductResponseDto() {
        CartProductResponseDto cartProductResponseDto = new CartProductResponseDto();
        cartProductResponseDto.setProductId(this.productId);
        cartProductResponseDto.setProductName(this.productName);
        cartProductResponseDto.setPricePerProduct(this.pricePerProduct);
        cartProductResponseDto.setQuantity(this.quantity);
        cartProductResponseDto.setSubTotalPrice(this.subTotalPrice);
        cartProductResponseDto.setMessage(this.message);
//
//        cartProductResponseDto.setStoreName(store.getStoreDetail().getName());
//        cartProductResponseDto.setEstimatedCookingTime(store.getStoreDetail().getEstimatedCookingTime());


        return cartProductResponseDto;
    }




}
