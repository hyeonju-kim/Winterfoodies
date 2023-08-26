package com.winterfoodies.winterfoodies_project.social;
import com.winterfoodies.winterfoodies_project.config.JwtUtil;
import com.winterfoodies.winterfoodies_project.entity.User;
import com.winterfoodies.winterfoodies_project.repository.UserRepository;
import com.winterfoodies.winterfoodies_project.service.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    private final List<SocialOauth> socialOauthList;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder encoder;

    // 1. redirectURL 만들기
    public String request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        return socialOauth.getOauthRedirectURL();
    }

    // 2. 액세스 토큰 만들기
    public ResponseEntity<String> requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = findSocialOauthByType(socialLoginType);
        return socialOauth.requestAccessToken(code);
    }

    // 3. 소셜 타입 찾기
    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new OAuthException("알 수 없는 SocialLoginType 입니다."));
    }

    // 4. 클라이언트로 보낼 GetSocialOAuthRes 객체 만들기
    public GetSocialOAuthRes oAuthLogin(SocialLoginType socialLoginType, String code) throws IOException {

        SocialOauth socialOauth = findSocialOauthByType(socialLoginType);
        ResponseEntity<String> accessTokenResponse = socialOauth.requestAccessToken(code);
        log.info("accessTokenResponse : {}", accessTokenResponse);

        //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
        SocialOauthToken oAuthToken = socialOauth.getAccessToken(accessTokenResponse);
        log.info("oAuthToken = {}", oAuthToken);

        //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
        ResponseEntity<String> userInfoResponse = socialOauth.requestUserInfo(oAuthToken);
        log.info("userInfoResponse = {}", userInfoResponse);

        //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
        SocailUser socailUser = socialOauth.getUserInfo(userInfoResponse);
        log.info("socailUser = {}", socailUser);
        log.info("socailUser.getEmail = {}", socailUser.getEmail());
        log.info("socailUser.getName = {}", socailUser.getName());

        String userId = socailUser.getEmail();
        String name = socailUser.getName();

        // 사용자가 존재하든 안하든 항상 JWT 토큰을 발급
        User user = new User(name, userId);
        user.setPassword(encoder.encode(userId)); //이메일을 비밀번호로 사용

        // DB에 해당 유저가 있는지 조회 후 없으면 save
        if (userRepository.findByUsername(userId)==null) {
            userRepository.save(user);
        }

        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        String jwtToken = jwtUtil.generateAccessToken(userDetails);
        log.info("jwtToken : {}", jwtToken);

        // 액세스 토큰과 위에서 만든 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
        return new GetSocialOAuthRes(jwtToken, userId, oAuthToken.getAccess_token(), oAuthToken.getToken_type());
    }
}

