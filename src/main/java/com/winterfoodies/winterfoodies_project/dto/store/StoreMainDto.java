package com.winterfoodies.winterfoodies_project.dto.store;

import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StoreMainDto { // 가게 상세 조회
    private String storeName;
    private Long averageRating;
    private String like = "N";
    private List<ProductResponseDto> productResponseDtoList;
    private List<ProductResponseDto> popularProductsDtoList;

}
