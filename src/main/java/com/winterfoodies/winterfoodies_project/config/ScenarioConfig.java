package com.winterfoodies.winterfoodies_project.config;

import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ScenarioConfig {

    @Bean
    public UserDto loginUser(){
       UserDto userDto = new UserDto();
       userDto.setEmail("asdf@naver.com");
       userDto.setPassword("100825asa!");
       userDto.setName("장발장");
       userDto.setLatitude(37.381798); // 위도 -90 ~ 90    래위작~
       userDto.setLongitude(126.800944); // 경도 -180 ~ 180
       return userDto;
    }
}
