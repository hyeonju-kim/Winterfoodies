package com.winterfoodies.winterfoodies_project.dto.user;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Builder
public class UserRequestDto {

//    @ApiModelProperty(example = "비밀번호 확인" , hidden = true)
//    private String passwordConfirm;

    @NotBlank(message = "휴대폰 번호는 필수 입력 값입니다.")
    @ApiModelProperty(example = "휴대폰 번호" )
    private String phoneNumber;

    @NotBlank(message = "이메일은 필수 입력 값입니다.") // username에 email을 넣어 사용한다.
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @ApiModelProperty(example = "이메일" )
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문, 숫자, 특수문자를 사용하세요.")
    @ApiModelProperty(example = "비밀번호" )
    private String password;

//    @NotBlank(message = "사용자 이름은 필수 입력 값입니다.")
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "사용자 이름은 특수문자를 제외한 2~10자리여야 합니다.")
//    private String username;

    @ApiModelProperty(example = "사용자 위도" , hidden = true )
    private double latitude;

    @ApiModelProperty(example = "사용자 경도" , hidden = true )
    private double longitude;
}
