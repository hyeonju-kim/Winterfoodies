package com.winterfoodies.winterfoodies_project.dto.product;

import com.winterfoodies.winterfoodies_project.entity.Product;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductDto {
    private Long id;
    private Long quantity;
    private String message;
    private Long storeId;

    public ProductDto() {
    }

    public ProductDto(ProductRequestDto requestDto) {
        this.id= requestDto.getId();
        this.quantity = requestDto.getQuantity();
        this.storeId = requestDto.getStoreId();
    }

    public ProductDto(Product product) {
        this.id = product.getId();
    }

    public ProductResponseDto convertToProductResponseDto() {
        ProductResponseDto productResponseDto = new ProductResponseDto();
        productResponseDto.setId(this.id);
        productResponseDto.setQuantity(this.quantity);
        productResponseDto.setMessage(this.message);
        return productResponseDto;
    }
}
