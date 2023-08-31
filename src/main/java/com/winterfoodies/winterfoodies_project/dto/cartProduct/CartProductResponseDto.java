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
    private Long id;

    @ApiModelProperty(example = "1", value = "상품 id")
    private Long productId;

    @ApiModelProperty(example = "5", value = "주문 수량")
    private Long quantity;

    @ApiModelProperty(example = "7500", value = "총 주문 금액")
    private Long totalPrice;

    @ApiModelProperty(value = "메시지", hidden = true)
    private String message;

    @ApiModelProperty(example = "1", value = "가게명", hidden = true)
    private Long storeId;
}
