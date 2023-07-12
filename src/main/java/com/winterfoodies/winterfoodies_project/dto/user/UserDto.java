package com.winterfoodies.winterfoodies_project.dto.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.winterfoodies.winterfoodies_project.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String username;
    private double latitude;
    private double longitude;


    //잘못된경우에만
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
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    // UserDto -> UserResponseDto (Dto에서 responseDto로 변환해준다)
    public UserResponseDto converToUserResponseDto() {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(this.id);
        userResponseDto.setUsername(this.username);
        return userResponseDto;
    }

}
