package com.winterfoodies.winterfoodies_project.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.cartProduct.CartProductDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.ArrayList;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartDto {
    private Long id;

    private String name;

    private String storeName;

    private Long price;

    private Long quantity;

    private Long totalPrice;

    private String EstimatedCookingTime;

    private String thumbnailImgUrl;

    private ArrayList<CartProductDto> cartProductDtoList;

    public CartDto() {
    }



}
