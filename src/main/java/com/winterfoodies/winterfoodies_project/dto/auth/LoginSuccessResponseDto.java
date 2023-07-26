package com.winterfoodies.winterfoodies_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
public class LoginSuccessResponseDto {
    private String token;
    private String message = "로그인 성공";

    public LoginSuccessResponseDto(String token) {
        this.token = token;
    }

}
