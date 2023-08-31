package com.winterfoodies.winterfoodies_project.dto.auth;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
public class LoginSuccessResponseDto {

    @ApiModelProperty(example = "access 토큰" )
    private String token;

    @ApiModelProperty(example = "refresh 토큰" )
    private String refreshToken;

    @ApiModelProperty(example = "메시지" )
    private String message = "로그인 성공";

    public LoginSuccessResponseDto(String token) {
        this.token = token;
    }

    public LoginSuccessResponseDto(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

}
