package com.winterfoodies.winterfoodies_project.controller;

import com.winterfoodies.winterfoodies_project.config.JwtUtil;
import com.winterfoodies.winterfoodies_project.dto.user.LoginRequestDto;
import com.winterfoodies.winterfoodies_project.dto.user.LoginSuccessResponseDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserDto;
import com.winterfoodies.winterfoodies_project.dto.user.UserRequestDto;
import com.winterfoodies.winterfoodies_project.service.UserDetailsImpl;
import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
import com.winterfoodies.winterfoodies_project.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HomeController {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserServiceImpl userService;
    private final JwtUtil jwtUtil;

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponseDto> loginTest(@RequestBody LoginRequestDto loginRequestDto) {

        // 1. 인증 성공(회원저장소에 해당 이름이 있으면) 후 인증된 user의 정보를 갖고옴
        UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequestDto.getUsername());

        // 2. subject, claim 모두 UserDetails를 사용하므로 객체를 그대로 전달
        String token = jwtUtil.generateToken(userDetails);

        // 3. 생성된 토큰을 응답
        return ResponseEntity.ok(new LoginSuccessResponseDto(token));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserRequestDto userRequestDto) {
        userService.signUp(userRequestDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

}
