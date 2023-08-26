package com.winterfoodies.winterfoodies_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginDto {
    private String email;
    private String password;
    private String username;
    private String token;

    public LoginDto() {

    }

    public LoginDto(LoginRequestDto requestDto) {
//        this.email = requestDto.getEmail();
        this.password = requestDto.getPassword();
        this.username = requestDto.getUsername();
    }


}
