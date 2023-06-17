package com.winterfoodies.winterfoodies_project.dto.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {
    private String email;
    private String password;
    private String name;

    //잘못된경우에만
    private String message;
}
