package com.winterfoodies.winterfoodies_project.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class SocialLoginController {

    @GetMapping("/google")
    public String googleLogin(Authentication authentication) throws OAuth2AuthenticationException {
        OAuth2User user = (OAuth2User) authentication.getPrincipal();
        // 사용자 정보를 활용한 로그인 처리 로직 작성
        // 예시로 사용자의 이메일 정보를 반환
        String email = user.getAttribute("email");
        return "Logged in with Google. Email: " + email;
    }

}