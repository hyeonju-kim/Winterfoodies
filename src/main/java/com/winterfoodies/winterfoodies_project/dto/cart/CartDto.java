package com.winterfoodies.winterfoodies_project.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartDto {
    private Long id;

    private String name;

    private Long price;

    private Long quantity;

    private Long totalPrice;

    private String EstimatedCookingTime;

    public CartDto() {
    }



}
