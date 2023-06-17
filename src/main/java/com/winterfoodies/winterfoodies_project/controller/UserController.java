package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.config.JwtUtil;
import com.winterfoodies.winterfoodies_project.dto.UserRequestDto;
import com.winterfoodies.winterfoodies_project.dto.UserResponseDto;
import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> createLogin(@RequestBody UserRequestDto userRequestDto) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRequestDto.getName());// 인증된 user정보 가져옴

        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new UserResponseDto());

    }
}
