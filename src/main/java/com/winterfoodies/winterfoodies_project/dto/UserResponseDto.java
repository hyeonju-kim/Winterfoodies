package com.winterfoodies.winterfoodies_project.dto;

import lombok.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Builder
@ToString
@RequiredArgsConstructor
public class UserResponseDto {
    private String message;
    private String redirect;
    private String result;
    private Long id;
    private String email;
    private String name;
    private String token;


}
