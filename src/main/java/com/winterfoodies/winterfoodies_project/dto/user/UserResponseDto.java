package com.winterfoodies.winterfoodies_project.dto.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winterfoodies.winterfoodies_project.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserResponseDto{

    @ApiModelProperty(example = "메시지" )
    private String message;

    @ApiModelProperty(example = "리다이렉트?", hidden = true )
    private String redirect;

//    private String result;

    @ApiModelProperty(example = "유저 아이디", hidden = true )
    private Long id;

    @ApiModelProperty(example = "유저 이메일", hidden = true )
    private String email;

    @ApiModelProperty(example = "유저 네임", hidden = true )
    private String username;

    @JsonIgnore
    @ApiModelProperty(example = "유저 상태", hidden = true )
    private HttpStatus status;

    public UserResponseDto(String message) {
        this.message = message;
    }
}
