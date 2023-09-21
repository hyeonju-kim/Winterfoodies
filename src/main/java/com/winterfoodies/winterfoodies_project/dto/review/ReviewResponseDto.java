package com.winterfoodies.winterfoodies_project.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.Timestamped;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReviewResponseDto extends Timestamped {
    private double userId;

    private String storeName;

    private double rating;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private List<UrlDto> images;

    private String content;
    private LocalDateTime timestamp;

    private String message; // 리뷰가 등록되었습니다.
}
