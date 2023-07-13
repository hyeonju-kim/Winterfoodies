package com.winterfoodies.winterfoodies_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequestDto {

    private String email;
    private String password;
    private String username;

}
