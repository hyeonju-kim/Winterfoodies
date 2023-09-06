package com.winterfoodies.winterfoodies_project.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class ProductDto {

    @ApiModelProperty(example = "상품 id" )
    private Long productId;

    @ApiModelProperty(example = "상품명" )
    private String productName;

    @ApiModelProperty(example = "상품 수량" )
    private Long quantity;

    @ApiModelProperty(example = "메시지" )
    private String message;

    @ApiModelProperty(example = "가게명" )
    private Long storeId;

    @ApiModelProperty(example = "500", value = "각 상품당 가격")
    private Long pricePerProduct;

    @ApiModelProperty(example = "3000", value = "각 메뉴별 총 주문금액")
    private Long subTotalAmount;

    public ProductDto() {
    }

    public ProductDto(ProductRequestDto requestDto) {
        this.productId= requestDto.getProductId();
        this.quantity = requestDto.getQuantity();
        this.storeId = requestDto.getStoreId();
    }

    public ProductDto(Product product) {
        this.productId = product.getId();
    }

    public ProductResponseDto convertToProductResponseDto() {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(this.productId);
        productResponseDto.setQuantity(this.quantity);
        productResponseDto.setMessage(this.message);

        return productResponseDto;
    }
}
