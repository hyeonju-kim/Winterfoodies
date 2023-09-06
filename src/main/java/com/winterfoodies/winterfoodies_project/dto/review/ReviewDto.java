package com.winterfoodies.winterfoodies_project.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class ReviewDto extends Timestamped {
    @ApiModelProperty(example = "1",value = "리뷰 id" )
    private  Long id;

    @ApiModelProperty(value = "사용자 id" , hidden = true)
    private Long userId;

    @ApiModelProperty(value = "사용자 닉네임" , example = "붕어조아")
    private String nickname;

    private Long orderId;

    @ApiModelProperty(example = "신천붕어빵", value = "가게명" )
    private String storeName;

    @ApiModelProperty(example = "1", value = "가게 id" )
    private Long storeId;

    @ApiModelProperty(example = "5", value = "별점" )
    private Long rating;

    @ApiModelProperty(value = "음식 사진" )
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String images;

    private List<String> orderedProducts; // 주문한 음식 리스트


    @ApiModelProperty(example = "완전 맛있어요ㅜㅜ", value = "리뷰 내용" )
    private String content;

    @ApiModelProperty(value = "작성 시간" )
    private LocalDateTime reviewTime;

    @ApiModelProperty(value = "주문 시간" )
    private LocalDateTime orderTime;

    @ApiModelProperty(value = "메시지" , hidden = true)
    private String message; // 리뷰가 등록되었습니다.

    private Order order;

    private Store store;

    private List<OrderProduct> orderProducts = new ArrayList<>();

    public ReviewDto(ReviewRequestDto requestDto) {
        this.rating = requestDto.getRating();
        this.images = requestDto.getImages();
        this.content = requestDto.getContent();
        this.createdAt = LocalDateTime.now(); // 현재 시간을 설정
        this.modifiedAt = LocalDateTime.now(); // 현재 시간을 설정
    }

    public ReviewDto(Review review) {
        this.rating = review.getRating();
        this.id = review.getId();
//        // 사진이 첨부되어 있지 않으면 빈 리스트 저장하고, 사진이 첨부되어 있으면 리스트로 저장하기
//        if (review.getImages().isEmpty() || review.getImages()==null) {
//            this.images = Collections.emptyList();
//        }else {
//            // 이미지 URL들을 쉼표(,)로 분할하여 List에 추가
//            String[] split = review.getImages().split(",");
//            for (String s : split) {
//                this.images.add(new UrlDto(s));
//            }
//        }
        this.images = review.getImages();
        this.content = review.getContent();
        this.userId = review.getUserId();
        this.storeId = review.getStore().getId();
        this.createdAt = LocalDateTime.now(); // 현재 시간을 설정
        this.modifiedAt = LocalDateTime.now(); // 현재 시간을 설정
        this.order = review.getOrder();
        this.store = review.getStore();

    }

    public ReviewResponseDto convertToReviewResponseDto() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
//        reviewResponseDto.setImages(this.images);
        reviewResponseDto.setContent(this.content);
        reviewResponseDto.setRating(this.rating);

        return reviewResponseDto;
    }

    public ReviewDto() {

    }
}
