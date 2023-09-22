package com.winterfoodies.winterfoodies_project.dto.store;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.product.ProductResponseDto;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
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

    @ApiModelProperty(example = "상품명" )
    private String productName;

    @ApiModelProperty(example = "평균별점" )
    private Long averageRating;

    @ApiModelProperty(example = "Y" )
    private String like = "N";

    @ApiModelProperty(example = "23" )
    private Long reviewCnt;

    @ApiModelProperty(example = "6" )
    private Long searchCnt;

    @ApiModelProperty(value = "가게썸네일사진", hidden = true)
    private String thumbNailImgUrl;

    @ApiModelProperty(example = "20분~30분" , value = "조리예상시간")
    private String estimatedCookingTime;

    private List<ProductResponseDto> productResponseDtoList;

    //상품 상수 9개 추가
    private List<String> productEnumArrayList;

    private List<ProductResponseDto> productConstantsList;

    private List<ProductResponseDto> popularProductsDtoList;
    private List<StoreResponseDto> storeResponseDtoList;
    private List<ReviewDto> reviewDtoList;

    private StoreResponseDto storeResponseDto;

}
