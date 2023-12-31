package com.winterfoodies.winterfoodies_project.entity;


import com.winterfoodies.winterfoodies_project.dto.store.StoreRequestDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "STORE_DETAIL")
@Getter
@Setter
@SequenceGenerator(name = "storeDetailSeq", sequenceName = "STORE_DETAIL_SEQ", initialValue = 1, allocationSize = 1)
public class StoreDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "storeDetailSeq")
    @Column(name = "STORE_DETAIL_ID")
    private Long id;

    @Column(name = "STORE_NAME")
    private String name;

    @Column(name = "STORE_ADDRESS_NO")
    private String addressNo;

    @Column(name = "STORE_BASIC_ADDRESS")
    private String basicAddress;

    @Column(name = "STORE_DETAIL_ADDRESS")
    private String detailAddress;

    @Column(name = "STORE_ROAD_CODE_NO")
    private String roadCodeNo;

    @Column(name = "STORE_OFFICIAL_CODE_NO")
    private String officialCodeNo;

    @Column(name = "STORE_OPEN_TIME")
    private LocalTime openTime;

    @Column(name = "STORE_CLOSE_TIME")
    private LocalTime closeTime;

    @Column(name = "AVERAGE_RATING")
    private Long averageRating;

    @Column(name = "LATITUDE")
    private double latitude;

    @Column(name = "LONGITUDE")
    private double longitude;

    @Lob
    @Column(name = "STORE_INFO")
    private String info;

    @Column(name = "ESTIMATED_COOKING_TIME")
    private String estimatedCookingTime;

    @Column(name = "STORE_THUMBNAIL_IMGURL")
    private String thumbnailImgUrl;

    private String mapIcon;

    @OneToOne
    private Store store;

    @OneToMany(mappedBy = "storeDetail")
    private List<Product> productsList = new ArrayList<>();


    private String status; //현재 영업 상태 -> TODO enum으로 바꾸기 -> 영업중, 영업종료

    private String openDate; //영업 요일  -> TODO enum으로 바꾸기 -> 매일, 주5일, 주말


    public void fillValue(StoreRequestDto storeRequestDto){
        this.name = StringUtils.hasText(storeRequestDto.getName()) ? storeRequestDto.getName() : name;
        this.addressNo = StringUtils.hasText(storeRequestDto.getAddressNo()) ? storeRequestDto.getAddressNo() : addressNo;
        this.info = StringUtils.hasText(storeRequestDto.getInfo()) ? storeRequestDto.getInfo() : info;
        this.basicAddress =  StringUtils.hasText(storeRequestDto.getBasicAddress()) ?  storeRequestDto.getBasicAddress() : basicAddress;
        this.detailAddress =  StringUtils.hasText(storeRequestDto.getDetailAddress()) ? storeRequestDto.getDetailAddress() : detailAddress;
        this.officialCodeNo =  StringUtils.hasText(storeRequestDto.getOfficialCodeNo()) ? storeRequestDto.getOfficialCodeNo() : officialCodeNo;
        this.roadCodeNo =  StringUtils.hasText(storeRequestDto.getRoadCodeNo()) ? storeRequestDto.getRoadCodeNo() : roadCodeNo;
        this.openTime =  !Objects.isNull(storeRequestDto.getOpenTime()) ? storeRequestDto.getOpenTime()  : openTime;
        this.closeTime =  !Objects.isNull(storeRequestDto.getCloseTime()) ? storeRequestDto.getCloseTime() : closeTime;
        this.thumbnailImgUrl =  StringUtils.hasText(storeRequestDto.getThumbnailImgUrl()) ? storeRequestDto.getThumbnailImgUrl() : thumbnailImgUrl;
    }

}
