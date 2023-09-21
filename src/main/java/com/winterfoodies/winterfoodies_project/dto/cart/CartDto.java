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
    private double id;

    private String name;

    private String storeName;

    private double price;

    private double quantity;

    private double totalPrice;

    private String EstimatedCookingTime;

    private String thumbnailImgUrl;

    private ArrayList<CartProductDto> cartProductDtoList;

    public CartDto() {
    }



}
