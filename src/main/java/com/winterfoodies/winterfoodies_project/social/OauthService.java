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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OauthService {
    private final List<SocialOauth> socialOauthList;
//    private final HttpServletResponse response;
    private final GoogleOauth googleOauth;
    private final KakaoOauth kakaoOauth;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 1. redirectURL 만들기
    public String request(SocialLoginType socialLoginType) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        return socialOauth.getOauthRedirectURL();

//        String redirectURL = socialOauth.getOauthRedirectURL();
//        try {
//            response.sendRedirect(redirectURL);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    // 2. 액세스 토큰 만들기
    public ResponseEntity<String> requestAccessToken(SocialLoginType socialLoginType, String code) {
        SocialOauth socialOauth = this.findSocialOauthByType(socialLoginType);
        return socialOauth.requestAccessToken(code);
    }

    // 3. 소셜 타입 찾기
    private SocialOauth findSocialOauthByType(SocialLoginType socialLoginType) {
        return socialOauthList.stream()
                .filter(x -> x.type() == socialLoginType)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("알 수 없는 SocialLoginType 입니다."));
    }

    // 4. 클라이언트로 보낼 GetSocialOAuthRes 객체 만들기
    public GetSocialOAuthRes oAuthLogin(SocialLoginType socialLoginType, String code) throws IOException {
        switch (socialLoginType) {
            case GOOGLE: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = googleOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                GoogleOAuthToken oAuthToken = googleOauth.getAccessToken(accessTokenResponse);
                log.info("oAuthToken = {}", oAuthToken);

                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse = googleOauth.requestUserInfo(oAuthToken);
                log.info("userInfoResponse = {}", userInfoResponse);

                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                GoogleUser googleUser = googleOauth.getUserInfo(userInfoResponse);
                log.info("googleUser = {}", googleUser);
                log.info("googleUser.getEmail = {}", googleUser.getEmail());
                log.info("googleUser.getName = {}", googleUser.getName());

                String user_id = googleUser.getEmail();
                String name = googleUser.getName();

                //우리 서버의 db와 대조하여 해당 user가 존재하는 지 확인한다.   -> 꼭 확인해야하나?? 확인 안해도 상관없지않나?
//                User user = userRepository.findByUsername(name);

//                if (user != null) {
//                    //서버에 user가 존재하면 앞으로 회원 인가 처리를 위한 jwtToken을 발급한다.
//                    Claims claims = Jwts.claims();
//                    claims.put("username", name);
//
//                    String jwtToken = jwtUtil.createToken(claims, user_id);
//
//                    //액세스 토큰과 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
//                    GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, 1, oAuthToken.getAccess_token(), oAuthToken.getToken_type());
//                    return getSocialOAuthRes;
//                } else {
//                    throw new IllegalArgumentException("실패");
//                }

                // 사용자가 존재하든 안하든 항상 JWT 토큰을 발급
//                User user = new User(name, user_id);  // user_id가 자꾸 null이 나와서 일단 주석처리;; 230728 TODO email 값 가져올 것!!!!!!!!!!! (  )
                User user = new User(name, "mjmj123@naver.com");

                user.setPassword("mjmj123!@#");  // 일단 추가;; TODO 빼기 (  )
                userRepository.save(user); //추가
                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                String jwtToken = jwtUtil.generateToken(userDetails);

                // 액세스 토큰과 위에서 만든 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
                String user_num = user_id + name;
                GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, user_num, oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                return getSocialOAuthRes;


            }case KAKAO: {
                //구글로 일회성 코드를 보내 액세스 토큰이 담긴 응답객체를 받아옴
                ResponseEntity<String> accessTokenResponse = kakaoOauth.requestAccessToken(code);
                //응답 객체가 JSON형식으로 되어 있으므로, 이를 deserialization해서 자바 객체에 담을 것이다.
                KakaoOAuthToken oAuthToken = kakaoOauth.getAccessToken(accessTokenResponse);
                log.info("oAuthToken = {}", oAuthToken);

                //액세스 토큰을 다시 구글로 보내 구글에 저장된 사용자 정보가 담긴 응답 객체를 받아온다.
                ResponseEntity<String> userInfoResponse = kakaoOauth.requestUserInfo(oAuthToken);
                log.info("userInfoResponse = {}", userInfoResponse);

                //다시 JSON 형식의 응답 객체를 자바 객체로 역직렬화한다.
                KakaoUser kakaoUser = kakaoOauth.getUserInfo(userInfoResponse);
                log.info("kakaoUser = {}", kakaoUser);
                log.info("kakaoUser.getEmail = {}", kakaoUser.getEmail());
                log.info("kakaoUser.getName = {}", kakaoUser.getName());

                String user_id = kakaoUser.getEmail();
                String name = kakaoUser.getName();

                // 사용자가 존재하든 안하든 항상 JWT 토큰을 발급
//                User user = new User(name, user_id);  // user_id가 자꾸 null이 나와서 일단 주석처리;; 230728 TODO email 값 가져올 것!!!!!!!!!!! (  )
                User user = new User(name, "mjmj123@naver.com");

                user.setPassword("mjmj123!@#");  // 일단 추가;; TODO 빼기 (  )
                userRepository.save(user); //추가
                UserDetailsImpl userDetails = new UserDetailsImpl(user);

                String jwtToken = jwtUtil.generateToken(userDetails);

                // 액세스 토큰과 위에서 만든 jwtToken, 이외 정보들이 담긴 자바 객체를 다시 전송한다.
                String user_num = user_id + name;
                GetSocialOAuthRes getSocialOAuthRes = new GetSocialOAuthRes(jwtToken, user_num, oAuthToken.getAccess_token(), oAuthToken.getToken_type());
                return getSocialOAuthRes;

            }
            default: {
                throw new IllegalArgumentException("알 수 없는 소셜 로그인 형식입니다.");
            }

        }
    }
}