package com.winterfoodies.winterfoodies_project.dto.product;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProductResponseDto {
    private Long id;
    private String productName;
    private Long price;
    private Long quantity;
    private String message;
}
