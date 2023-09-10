package com.winterfoodies.winterfoodies_project.dto.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LoginSuccessResponseDto {

    @ApiModelProperty(example = "access 토큰" )
    private String accessToken;

    @ApiModelProperty(example = "refresh 토큰" )
    private String refreshToken;

    private UserResponseDto userResponseDto;

//    @ApiModelProperty(example = "refresh 토큰" )
//    private String refreshToken;



    public LoginSuccessResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public LoginSuccessResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }



}
