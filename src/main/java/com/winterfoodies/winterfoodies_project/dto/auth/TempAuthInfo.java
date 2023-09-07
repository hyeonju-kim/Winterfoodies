package com.winterfoodies.winterfoodies_project.dto.auth;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;


@Getter
@Setter
public class TempAuthInfo {
    @Id
    private String email;
    private String tempPassword;
}
