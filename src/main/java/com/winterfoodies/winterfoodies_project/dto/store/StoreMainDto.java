package com.winterfoodies.winterfoodies_project.dto.store;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StoreMainDto { // 가게 상세 조회
    @ApiModelProperty(example = "가게명" )
    private String storeName;

    @ApiModelProperty(example = "평균별점" )
    private Long averageRating;

    @ApiModelProperty(example = "찜유무" )
    private String like = "N";

    @ApiModelProperty(example = "20분~30분" , value = "조리예상시간")
    private String estimatedCookingTime;

    private List<ProductResponseDto> productResponseDtoList;
    private List<ProductResponseDto> popularProductsDtoList;
    private List<StoreResponseDto> storeResponseDtoList;

    private StoreResponseDto storeResponseDto;

}
