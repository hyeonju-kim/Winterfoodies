//package com.winterfoodies.winterfoodies_project.config;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//
//@Component
//public class JwtUtil {
//    private final String SECRET = "secret";
//
//    //토큰 생성
//    public String generateToken(UserDetails userDetails) {
//        Claims claims = Jwts.claims();
//        claims.put("username", userDetails.getUsername());
//        return createToken(claims, userDetails.getUsername());
//    }
//
//    private String createToken(Claims claims, String subject) {
//        return Jwts.builder()
//            .setClaims(claims)
//            .setIssuedAt(new Date(System.currentTimeMillis()))
//            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//            .signWith(SignatureAlgorithm.ES256, SECRET)
//            .compact();
//    }
//
//    public Boolean isValidToken(String token, UserDetails userDetails){
//        String username = getUsernameFromToken(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }
//
//    private Claims getAllClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET)
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public String getUsernameFromToken(String token) {
//        String username = String.valueOf(getAllClaims(token).get("username"));
//        return username;
//    }
//
//    public Date getExpirationDate(String token) {
//        Claims claims = getAllClaims(token);
//        return claims.getExpiration();
//    }
//
//    public boolean isTokenExpired(String token) {
//        return getExpirationDate(token).before(new Date());
//    }
//
//}
//
