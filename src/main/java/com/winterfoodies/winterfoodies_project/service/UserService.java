package com.winterfoodies.winterfoodies_project.service;

import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


public interface UserService {
    //유저 정보 조회
    public UserDto retrieveUser();

    //유저 비밀번호 변경
    public UserDto changePw(UserDto userDto);
}
