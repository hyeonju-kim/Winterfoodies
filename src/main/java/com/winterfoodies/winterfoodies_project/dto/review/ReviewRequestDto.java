package com.winterfoodies.winterfoodies_project.dto.review;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReviewRequestDto {
    private Long userId;
    private String storeName;
    private Long rating;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private String images;

    private String content;

    private LocalDateTime timestamp;

    private String message; // 리뷰가 등록되었습니다.
}
