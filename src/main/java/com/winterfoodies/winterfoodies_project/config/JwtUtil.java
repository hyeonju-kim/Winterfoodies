package com.winterfoodies.winterfoodies_project.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET = "secret";

    // 1. 토큰 생성
    public String generateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Claims claims, String subject) {
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
            .signWith(SignatureAlgorithm.HS256, SECRET)
            .compact();
    }

    // 2. 토큰 유효여부 확인
    public Boolean isValidToken(String token, UserDetails userDetails){
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 3. 토큰의 claim 디코딩
    private Claims getAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    // 4. 토큰에서 username 가져오기
    public String getUsernameFromToken(String token) {
        String username = String.valueOf(getAllClaims(token).get("username"));
        return username;
    }

    // 5. 토큰 만료기한 가져오기
    public Date getExpirationDate(String token) {
        Claims claims = getAllClaims(token);
        return claims.getExpiration();
    }

    // 6. 토큰 만료여부 확인
    public boolean isTokenExpired(String token) {
        return getExpirationDate(token).before(new Date());
    }
}

