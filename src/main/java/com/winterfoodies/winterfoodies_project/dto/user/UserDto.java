package com.winterfoodies.winterfoodies_project.dto.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winterfoodies.winterfoodies_project.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class UserDto {
    @ApiModelProperty(example = "1" ,hidden = true)
    private Long id;
//    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문, 숫자, 특수문자를 사용하세요.")
    @ApiModelProperty(example = "asdf123!@#" )
    private String password;

    @ApiModelProperty(example = "asdf123@naver.com" ,hidden = true)
    private String username;

    @ApiModelProperty(example = "붕어빵러버",hidden = true)
    private String nickname;

    @ApiModelProperty(example = "37.381798" ,hidden = true)
    private double latitude;

    @ApiModelProperty(example = "126.800944" ,hidden = true)
    private double longitude;


    //잘못된경우에만
    @ApiModelProperty(example = "메시지" ,hidden = true)
    private String message;

    public UserDto() {
    }

    // request -> service (requestDto를 받아서 Dto로 변환해준다)
    public UserDto(UserRequestDto requestDto) {
        this.password = requestDto.getPassword();
        this.username = requestDto.getUsername();
    }

    // repository -> service
    public UserDto(User user) {
//        this.email = user.getEmail();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.nickname = user.getNickname();
    }

    // UserDto -> UserResponseDto (Dto에서 responseDto로 변환해준다)
    public UserResponseDto convertToUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(this.id);
        userResponseDto.setUsername(this.username);
        userResponseDto.setNickname(this.nickname);
        userResponseDto.setMessage(this.message);
        return userResponseDto;
    }

}
