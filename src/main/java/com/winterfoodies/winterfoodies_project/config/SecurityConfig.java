package com.winterfoodies.winterfoodies_project.config;

import com.winterfoodies.winterfoodies_project.config.JwtFilter;
import com.winterfoodies.winterfoodies_project.config.JwtUtil;
import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class  SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    private static final String[] AUTH_WHITE_LIST = {
            "/v2/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/swagger/**",
            "/swagger-ui/**",
            "/h2/**",
            "/css/**",
            "/js/**",
            "/scss/**",
            "/vendor/**",
            "/img/**",
            "/store-img/**",
            "/api/mypage",
            "/h2-console/**",
            "/api/login"
    };

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService); //1. 내가 만든 서비스 지정
    }

    // 2. 시큐리티 필터 적용하지 않을 url (우회용도)
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITE_LIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 3. 요청에 대해 인증 없이 접근을 허용하는 설정. 이 설정을 사용하면 모든 URL에 대해 인증되지 않은 사용자도 접근할 수 있다.
        http.csrf().disable()
                .authorizeRequests().antMatchers("/**").permitAll()// 괄호 안에 허용해줄 url적기
                .anyRequest().authenticated();

        http.headers().frameOptions().disable(); // h2 db 접근위해 추가함 (빼면 화면안보임)

        // 4. 세션 꺼주기
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // 5. jwt필터 넣어주기
        // UsernamePasswordAuthenticationFilter에 도달하기 전에 커스텀한 필터를 먼저 동작시킴
        http.addFilterBefore(new JwtFilter(userDetailsService, jwtUtil),UsernamePasswordAuthenticationFilter.class);
    }

}
