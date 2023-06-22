package com.winterfoodies.winterfoodies_project.dto.user;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //비어있지 않은 필드만 나타내는 어노테이션
public class UserDto {
    private Long id;
    private String email;
    private String password;
    private String name;
    private double latitude;
    private double longitude;


    //잘못된경우에만
    private String message;
}
