package com.winterfoodies.winterfoodies_project.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRequestDto {
    private String email;
    private String password;
    private String name;
}
