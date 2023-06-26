package com.winterfoodies.winterfoodies_project.config;

import com.winterfoodies.winterfoodies_project.AppRunner;
import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Security;

@RequiredArgsConstructor
//@Component
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization"); // 1. 헤더 파싱
        String username = "";
        String token = "";

        if (authorization != null && authorization.startsWith("Bearer ")) { // 2. 토큰 파싱 해서 name 얻어오기
            token = authorization.substring(7);
            username = jwtUtil.getUsernameFromToken(token);
        }else{
            // 로그인 요청은 필터를 거치지 않고 허용
//            if (request.getRequestURI().equals("/api/login")) {
                filterChain.doFilter(request, response);
//                return;
//            }
        }

        // 3. 토큰은 있는데 현재 인증객체가 없으면 컨텍스트홀더에 유저토큰 세팅
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 토큰 유효여부 확인
            if (jwtUtil.isValidToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }


}
