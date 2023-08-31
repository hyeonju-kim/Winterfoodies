package com.winterfoodies.winterfoodies_project.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TokenRequestDto {

    @ApiModelProperty(example = "access 토큰" )
    private String accessToken;

    @ApiModelProperty(example = "refresh 토큰" )
    private String refreshToken;
}
