package com.winterfoodies.winterfoodies_project.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginSuccessResponseDto {
    private String token;
}
