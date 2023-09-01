package com.winterfoodies.winterfoodies_project.dto.store;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.entity.Store;
import com.winterfoodies.winterfoodies_project.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class StoreDto {
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
    private double latitude;
    private double longitude;
    private String estimatedCookingTime;

    public StoreDto() {

    }

    // request -> service (requestDto를 받아서 Dto로 변환해준다)
    public StoreDto(StoreRequestDto requestDto) {
        this.name = requestDto.getName();
        this.basicAddress = requestDto.getBasicAddress();
        this.averageRating = requestDto.getAverageRating();
    }

    // repository -> service
    public StoreDto(Store store) {
        this.name = store.getStoreDetail().getName();
        this.basicAddress = store.getStoreDetail().getBasicAddress();
        this.averageRating = store.getStoreDetail().getAverageRating();
    }

    // UserDto -> UserResponseDto (Dto에서 responseDto로 변환해준다)
    public StoreResponseDto convertToStoreResponseDto() {
        StoreResponseDto storeResponseDto = new StoreResponseDto();
        storeResponseDto.setId(this.id);
        storeResponseDto.setName(this.name);
        storeResponseDto.setBasicAddress(this.basicAddress);
        storeResponseDto.setAverageRating(this.averageRating);
        storeResponseDto.setLatitude(this.latitude);
        storeResponseDto.setLongitude(this.longitude);


        return storeResponseDto;
    }
}
