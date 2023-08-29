package com.winterfoodies.winterfoodies_project.dto.store;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.StoreDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class StoreResponseDto {
    private String message;
    private String redirect;
    private String result;
    private Long id;
    private String name; // 가게명
    private String addressNo;
    private String basicAddress; // 기본주소
    private String detailAddress;
    private String info;
    private String roadCodeNo;
    private Long averageRating; // 평균 별점
    private Long orders; // 판매량

    private String officialCodeNo;

    private LocalTime openTime;

    private LocalTime closeTime;

    private String hasStoreYn;

    private String thumbNailImgUrl;

    public void fllWithStoreDetail(StoreDetail storeDetail){
        this.name = storeDetail.getName();
        this.addressNo = storeDetail.getAddressNo();
        this.basicAddress = storeDetail.getBasicAddress();
        this.detailAddress = storeDetail.getDetailAddress();
        this.info = storeDetail.getInfo();
        this.openTime = storeDetail.getOpenTime();
        this.closeTime = storeDetail.getCloseTime();
        this.roadCodeNo = storeDetail.getRoadCodeNo();
        this.officialCodeNo =storeDetail.getOfficialCodeNo();
        this.thumbNailImgUrl = storeDetail.getThumbnailImgUrl();
    }

    public static StoreResponseDto empty(){
        StoreResponseDto store = new StoreResponseDto();
        store.setHasStoreYn("N");
        return store;
    }
}
