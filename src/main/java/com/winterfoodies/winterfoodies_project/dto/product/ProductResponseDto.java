package com.winterfoodies.winterfoodies_project.dto.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductResponseDto {
    @ApiModelProperty(example = "상품 아이디" )
    private Long id;

    @ApiModelProperty(example = "상품명" )
    private String productName;

    @ApiModelProperty(example = "상품 가격" )
    private Long price;

    @ApiModelProperty(example = "상품 수량" )
    private Long quantity;


    private String message;
}
