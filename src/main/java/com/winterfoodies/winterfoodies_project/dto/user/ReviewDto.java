package com.winterfoodies.winterfoodies_project.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.entity.Timestamped;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class ReviewDto extends Timestamped {
    private Long userId;

    private String storeName;

    private Long rating;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private byte[] photo;

    private String content;
    private LocalDateTime timestamp;

    private String message; // 리뷰가 등록되었습니다.
}
