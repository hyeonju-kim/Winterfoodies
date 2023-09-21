package com.winterfoodies.winterfoodies_project.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.review.ReviewDto;
import com.winterfoodies.winterfoodies_project.dto.review.UrlDto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.persistence.GenerationType;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Review extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private double id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "STORE_ID")
    private Store store;
//    private double storeId;
    private String storeName;
    private double rating;
    private double userId;
    private String images;// 이미지 URL들을 쉼표(,) 또는 다른 구분자로 구분하여 저장
    private String content;

    @OneToOne
    private Order order;
//    private LocalDateTime timestamp;  -> 이건 추가할 필요 없음!!!!!!  reviewDto에는 시작시간 담아야하니까 추가해준것

    public Review(ReviewDto reviewDto) {
        this.rating = reviewDto.getRating();
        this.content = reviewDto.getContent();
        this.images = reviewDto.getImages();
//        // 첨부한 사진 파싱해서 String으로 저장하기
//        if (reviewDto.getImages().isEmpty() || reviewDto.getImages()==null){
//            this.images = "첨부 이미지 없음";
//        }else {
//            List<UrlDto> images = reviewDto.getImages();
//            System.out.println("image List === " + images.toString());
//
//            StringBuilder imageUrlsBuilder = new StringBuilder();
//            for (UrlDto urlDto : images) {
//                if (urlDto.getUrl().length() > 0) {
//                    System.out.println("urlDto.getUrl() === " + urlDto.getUrl());
//                    imageUrlsBuilder.append(","); // 이미지 URL 사이에 쉼표 추가
//                }
//                imageUrlsBuilder.append(urlDto.getUrl()); // 이미지 URL 누적
//                System.out.println("imageUrlsBuilder === " + imageUrlsBuilder);
//            }
//            this.images = imageUrlsBuilder.toString(); // 최종적으로 쉼표로 구분된 문자열 얻음
//        }

//        this.storeId = reviewDto.getStoreId();
        this.storeName = reviewDto.getStoreName();
        this.userId = getUserId();
        this.createdAt = LocalDateTime.now(); // 현재 시간을 설정
        this.modifiedAt = LocalDateTime.now(); // 현재 시간을 설정
    }

    public Review() {

    }

}
