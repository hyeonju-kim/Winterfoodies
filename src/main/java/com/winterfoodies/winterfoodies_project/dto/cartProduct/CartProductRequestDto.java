package com.winterfoodies.winterfoodies_project.dto.cartProduct;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class CartProductRequestDto {
    private double id;
    private double productId;
    private String productName;
    private double quantity;
    private double subTotalPrice; // 장바구니의 각 상품별 합계
    private double totalPrice;
}
