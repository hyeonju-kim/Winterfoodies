package com.winterfoodies.winterfoodies_project.dto.store;

import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreMainDto {
    private List<ProductResponseDto> productResponseDtoList;
    private List<ProductResponseDto> popularProductsDtoList;

}
