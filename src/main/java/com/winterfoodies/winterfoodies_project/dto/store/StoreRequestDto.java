package com.winterfoodies.winterfoodies_project.dto.store;


import com.winterfoodies.winterfoodies_project.entity.StoreStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class StoreRequestDto {
    private String name;

    private String addressNo;

    private String basicAddress;

    private String detailAddress;

    private String roadCodeNo;

    private String officialCodeNo;

    private LocalDateTime openTime;

    private LocalDateTime closeTime;

    private String info;

    private String thumbnailImgUrl;

    private MultipartFile thumbnailImg;

    private StoreStatus status;

    private double latitude;

    private double longitude;
}
