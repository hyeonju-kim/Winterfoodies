package com.winterfoodies.winterfoodies_project.dto.cartProduct;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class CartProductResponseDto {
    @ApiModelProperty(example = "1", value = "CartProduct id", hidden = true)
    private double id;

    @ApiModelProperty(example = "1", value = "상품 id")
    private double productId;

    @ApiModelProperty(example = "어묵", value = "상품명")
    private String productName;

    @ApiModelProperty(example = "5", value = "주문 수량")
    private double quantity;

    @ApiModelProperty(example = "500", value = "각 상품당 가격")
    private double pricePerProduct;

    @ApiModelProperty(example = "2500", value = "각 상품별 합계")
    private double subTotalPrice; // 장바구니의 각 상품별 합계

    @ApiModelProperty(example = "7500", value = "총 주문 금액")
    private double totalPrice;

    @ApiModelProperty(value = "메시지", hidden = true)
    private String message;

    @ApiModelProperty(example = "1", value = "가게 id", hidden = true)
    private double storeId;

    @ApiModelProperty(example = "1", value = "가게명")
    private String storeName;

    @ApiModelProperty(example = "20분~30분" , value = "조리예상시간")
    private String estimatedCookingTime;
}
