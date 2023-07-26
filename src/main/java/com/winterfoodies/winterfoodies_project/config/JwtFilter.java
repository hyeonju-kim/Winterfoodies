package com.winterfoodies.winterfoodies_project.config;

import com.winterfoodies.winterfoodies_project.AppRunner;
import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.http.ResponseEntity;
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
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. 헤더 파싱
        String authorization = request.getHeader("Authorization");
        String username = "";
        String token = "";

        // 2. 토큰 파싱 해서 name 얻어오기
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
            username = jwtUtil.getUsernameFromToken(token);

            log.info("username = {}", username);

        }else{
                filterChain.doFilter(request, response);

                // 추가!! IllegalStateException: Cannot call sendRedirect() after the response has been committed 해결 위해 추가함. 비 로그인 상태일 경우 소셜로그인창을 두번 호출해서 발생하는 에러임
                return;
        }

        // 3. 토큰은 있는데 현재 인증객체가 없으면 컨텍스트홀더에 유저토큰 세팅
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info("userDatails = {}", userDetails);

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
