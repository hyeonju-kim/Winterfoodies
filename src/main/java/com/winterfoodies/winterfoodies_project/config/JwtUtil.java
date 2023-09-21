package com.winterfoodies.winterfoodies_project.config;

import com.winterfoodies.winterfoodies_project.service.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final String SECRET = "secret";
    private static final double ACCESS_TOKEN_EXPIRATION = 60 * 30 * 1000;     // 30분 (밀리초 단위로 표현)
    private static final double REFRESH_TOKEN_EXPIRATION = 60L * 60 * 24 * 90 * 1000; // 3개월 (밀리초 단위로 표현)



    // 액세스 토큰 생성
    public String generateAccessToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        System.out.println("유저디테일 겟네임===="+ userDetails.getUsername());
        return createToken(claims, userDetails.getUsername(), ACCESS_TOKEN_EXPIRATION);
    }

    // 230814 추가 - 리프레시 토큰 생성
    public String generateRefreshToken(UserDetails userDetails) {
        Claims claims = Jwts.claims();
        claims.put("username", userDetails.getUsername());
        return createToken(claims, userDetails.getUsername(), REFRESH_TOKEN_EXPIRATION);
    }

    private String createToken(Claims claims, String subject, double expirationMillis) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
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

    // 4. 토큰에서 email 가져오기
    public String getEmailFromToken(String token) {
        String email = String.valueOf(getAllClaims(token).get("email"));
        return email;
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
//
//    // 7. 토큰 만료기간을 0으로 만들어 토큰 무효화하기 (로그아웃)
//    public void setExpirationZero(String token) {
//        Claims claims = getAllClaims(token);
//        claims.setExpiration(new Date());
//    }
}

