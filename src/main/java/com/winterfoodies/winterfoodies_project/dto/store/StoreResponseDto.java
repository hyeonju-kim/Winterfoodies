package com.winterfoodies.winterfoodies_project.dto.store;


import com.winterfoodies.winterfoodies_project.entity.StoreDetail;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StoreResponseDto {
    private String message;
    private String redirect;
    private String result;
    private Long id;
    private String name;
    private String addressNo;
    private String basicAddress;
    private String detailAddress;
    private String info;
    private String roadCodeNo;

    private String officialCodeNo;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

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
