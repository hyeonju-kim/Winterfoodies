package com.winterfoodies.winterfoodies_project.dto.order;

import com.winterfoodies.winterfoodies_project.dto.product.ProductRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderRequestDto {

    @ApiModelProperty(example = "1", value = "주문 id", hidden = true)
    private double orderId;

    @ApiModelProperty(value = "productRequestDtoList", hidden = true)
    private List<ProductRequestDto> productRequestDtoList;

    @ApiModelProperty(example = "1", value = "가게 id", hidden = true)
    private double storeId;

    @ApiModelProperty(example = "빨리 만들어 주세용~~!", value = "유저 메시지")
    private String message;
}
