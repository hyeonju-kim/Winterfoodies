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

    @ApiModelProperty(example = "사용가능한 이메일 주소입니다.")
    private String message;

    @ApiModelProperty(hidden = true )
    private String redirect;

//    private String result;

    @ApiModelProperty(example = "1", hidden = true )
    private Long id;

//    @ApiModelProperty(example = "유저 이메일", hidden = true )
//    private String email;

    @ApiModelProperty(example = "asdf12345@naver.com", hidden = true)
    private String username;

    @ApiModelProperty(example = "붕어빵러버", hidden = true)
    private String nickname;

    @ApiModelProperty(example = "010-8711-7702", hidden = true)
    private String phoneNumber;

//    @JsonIgnore
    @ApiModelProperty(example = "success")
    private String status;

    public UserResponseDto(String message) {
        this.message = message;
    }

    public UserResponseDto(String message, String status) {
        this.message = message;
        this.status = status;
    }


    public UserResponseDto(String username, String nickname, String phoneNumber) {
        this.username = username;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
    }

    public static UserResponseDto createEmpty() {
        return new UserResponseDto(); // 빈 객체 반환
    }

}
