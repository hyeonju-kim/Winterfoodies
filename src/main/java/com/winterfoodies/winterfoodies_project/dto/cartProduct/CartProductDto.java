package com.winterfoodies.winterfoodies_project.dto.cartProduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.CartProduct;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class CartProductDto {
    private Long id;
    private Long productId;
    private Long quantity;
    private Long totalPrice;
    private String message;

    // 기본 생성자
    public CartProductDto() {

    }

    // 1. requestDto -> Dto
    public CartProductDto(CartProductRequestDto requestDto) {
        this.productId = requestDto.getProductId();
        this.quantity = requestDto.getQuantity();
        this.totalPrice = requestDto.getTotalPrice();
    }

    // 2. entity -> Dto
    public CartProductDto(CartProduct cartProduct) {
        this.id = cartProduct.getId();
        this.productId = cartProduct.getProduct().getId();
        this.quantity = cartProduct.getQuantity();
        this.totalPrice = cartProduct.getTotalPrice();
    }

    // 3. Dto -> responseDto
    public CartProductResponseDto convertToCartProductResponseDto() {
        CartProductResponseDto cartProductResponseDto = new CartProductResponseDto();
        cartProductResponseDto.setProductId(this.productId);
        cartProductResponseDto.setQuantity(this.quantity);
        cartProductResponseDto.setTotalPrice(this.totalPrice);
        cartProductResponseDto.setMessage(this.message);
        return cartProductResponseDto;
    }




}
