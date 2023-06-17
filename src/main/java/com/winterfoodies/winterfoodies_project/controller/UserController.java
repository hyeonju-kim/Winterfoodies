package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserResponseDto;
import com.winterfoodies.winterfoodies_project.service.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("")
    public UserDto getUser() {
        return userService.retrieveUser();
    }

    @PutMapping("/changepw")
    public UserDto changePw(@RequestBody UserDto userDto) {
        return userService.changePw(userDto);
    }
}
