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
       userDto.setEmail("blessdutch@naver.com");
       userDto.setPassword("100825asa!");
       userDto.setName("김현주");
       return userDto;
    }
}
