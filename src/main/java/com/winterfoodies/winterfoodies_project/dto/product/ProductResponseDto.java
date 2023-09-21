package com.winterfoodies.winterfoodies_project.dto.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductResponseDto {
    @ApiModelProperty(example = "1", value = "상품 아이디")
    private Long id;

    @ApiModelProperty(example = "붕어빵", value = "상품명", hidden = true )
    private String productName;

    @ApiModelProperty(example = "2000", value = "상품 가격", hidden = true )
    private Long price;

    @ApiModelProperty(example = "5", value = "상품 수량", hidden = true )
    private Long quantity;


    @ApiModelProperty(example = "장바구니에 추가되었습니다." )
    private String message;

    public ProductResponseDto(Long productId){
        this.id = productId;
    }

    public ProductResponseDto() {

    }
}
