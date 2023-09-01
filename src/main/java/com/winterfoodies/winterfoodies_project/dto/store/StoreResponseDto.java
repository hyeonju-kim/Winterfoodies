package com.winterfoodies.winterfoodies_project.dto.store;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.StoreDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class StoreResponseDto {
    @ApiModelProperty(example = "메시지", hidden = true)
    private String message;

    @ApiModelProperty(example = "리다이렉트?", hidden = true)
    private String redirect;

    @ApiModelProperty(example = "결과?", hidden = true)
    private String result;

    @ApiModelProperty(example = "1",value = "가게아이디" )
    private Long id;

    @ApiModelProperty(example = "신천붕어빵" , value = "가게명")
    private String name; // 가게명

    @ApiModelProperty(example = "우편번호", hidden = true)
    private String addressNo;

    @ApiModelProperty(example = "신천역 1번 출구",value = "기본주소")
    private String basicAddress; // 기본주소

    @ApiModelProperty(example = "상세주소", hidden = true)
    private String detailAddress;

    @ApiModelProperty(example = "가게정보", hidden = true)
    private String info;

    @ApiModelProperty(example = "도로명", hidden = true)
    private String roadCodeNo;

    @ApiModelProperty(example = "4", value = "평균별점")
    private Long averageRating; // 평균 별점

    @ApiModelProperty(value = "판매량", hidden = true)
    private Long orders; // 판매량

    @ApiModelProperty(value = "리뷰개수", hidden = true)
    private Long countReviews; // 리뷰개수

    @ApiModelProperty(example = "37.381798", value = "위도")
    private double latitude;

    @ApiModelProperty(example = "126.800944", value = "경도")
    private double longitude;

    @ApiModelProperty(example = "20분~30분" , value = "조리예상시간")
    private String estimatedCookingTime;


//    private String officialCodeNo;

    @ApiModelProperty(value = "오픈시간", hidden = true)
    private LocalTime openTime;

    @ApiModelProperty(value = "마감시간", hidden = true)
    private LocalTime closeTime;

    @ApiModelProperty(value = "가게소유여부", hidden = true)
    private String hasStoreYn;

    @ApiModelProperty(value = "가게썸네일사진", hidden = true)
    private String thumbNailImgUrl;

    public void fllWithStoreDetail(StoreDetail storeDetail){
        this.name = storeDetail.getName();
        this.addressNo = storeDetail.getAddressNo();
        this.basicAddress = storeDetail.getBasicAddress();
        this.detailAddress = storeDetail.getDetailAddress();
        this.info = storeDetail.getInfo();
        this.openTime = storeDetail.getOpenTime();
        this.closeTime = storeDetail.getCloseTime();
//        this.roadCodeNo = storeDetail.getRoadCodeNo();
//        this.officialCodeNo =storeDetail.getOfficialCodeNo();
        this.thumbNailImgUrl = storeDetail.getThumbnailImgUrl();
    }

    public static StoreResponseDto empty(){
        StoreResponseDto store = new StoreResponseDto();
//        store.setHasStoreYn("N");
        return store;
    }
}
