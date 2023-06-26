package com.winterfoodies.winterfoodies_project.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequestDto {

    private String email;
    private String password;
    private String username;

}
