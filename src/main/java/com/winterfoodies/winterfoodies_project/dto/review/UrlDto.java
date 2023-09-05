package com.winterfoodies.winterfoodies_project.dto.review;

import lombok.Data;

@Data
public class UrlDto {
    private String url;

    public UrlDto(String url) {
        this.url = url;
    }
}
