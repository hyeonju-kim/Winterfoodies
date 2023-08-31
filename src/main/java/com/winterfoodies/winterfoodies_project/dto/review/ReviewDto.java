package com.winterfoodies.winterfoodies_project.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.Review;
import com.winterfoodies.winterfoodies_project.entity.Timestamped;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class ReviewDto extends Timestamped {
    @ApiModelProperty(example = "1",value = "리뷰 id" )
    private  Long id;

    @ApiModelProperty(value = "사용자 id" , hidden = true)
    private Long userId;

    @ApiModelProperty(example = "신천붕어빵", value = "가게명" )
    private String storeName;

    @ApiModelProperty(example = "5", value = "별점" )
    private Long rating;

    @ApiModelProperty(value = "가게명" , hidden = true )
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private byte[] photo;

    @ApiModelProperty(example = "완전 맛있어요ㅜㅜ", value = "리뷰 내용" )
    private String content;

    @ApiModelProperty(value = "작성 시간" )
    private LocalDateTime timestamp;

    @ApiModelProperty(value = "메시지" , hidden = true)
    private String message; // 리뷰가 등록되었습니다.

    public ReviewDto(ReviewRequestDto requestDto) {
        this.rating = requestDto.getRating();
        this.photo = requestDto.getPhoto();
        this.content = requestDto.getContent();
    }

    public ReviewDto(Review review) {
        this.rating = review.getRating();
        this.photo = review.getPhoto();
        this.content = review.getContent();
    }

    public ReviewResponseDto convertToReviewResponseDto() {
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto();
        reviewResponseDto.setPhoto(this.photo);
        reviewResponseDto.setContent(this.content);
        reviewResponseDto.setRating(this.rating);
        return reviewResponseDto;
    }

    public ReviewDto() {

    }
}
