package com.winterfoodies.winterfoodies_project.dto.cart;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CartResponseDto {
    private double id;

    private String name;

    private double price;

    private double quantity;

    private double totalPrice;

    private String EstimatedCookingTime;

    public CartResponseDto() {
    }
}
