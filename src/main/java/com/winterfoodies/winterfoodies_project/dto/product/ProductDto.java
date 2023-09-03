package com.winterfoodies.winterfoodies_project.dto.product;

import com.winterfoodies.winterfoodies_project.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {

    @ApiModelProperty(example = "상품 id" )
    private Long productId;

    @ApiModelProperty(example = "상품 수량" )
    private Long quantity;

    @ApiModelProperty(example = "메시지" )
    private String message;

    @ApiModelProperty(example = "가게명" )
    private Long storeId;

    @ApiModelProperty(example = "500", value = "각 상품당 가격")
    private Long pricePerProduct;

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
