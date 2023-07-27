package com.winterfoodies.winterfoodies_project.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class KakaoOauth implements SocialOauth {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_SNS_CLIENT_ID;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_SNS_CALLBACK_URL;
    @Value("${spring.security.oauth2.client.registration.kakao.authorization-grant-type}")
    private String KAKAO_SNS_AUTH_TYPE;


    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    // 1. redirect Url 생성
    @Override
    public String getOauthRedirectURL() {
        Map<String, Object> params = new HashMap<>();
//        params.put("scope", "profile");
        params.put("response_type", "code");
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);

        String parameterString = params.entrySet().stream()
                .map(x -> x.getKey() + "=" + x.getValue())
                .collect(Collectors.joining("&"));

        return "https://kauth.kakao.com/oauth/authorize" + "?" + parameterString;
    }

    // 2. 코드 추가한 url 생성
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", KAKAO_SNS_CLIENT_ID);
//        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");
//
//        ResponseEntity<String> responseEntity =
//                restTemplate.postForEntity("https://kauth.kakao.com/oauth/authorize", params, String.class);

//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
//            return responseEntity;
//        }

        KakaoUser kakaoUser = new KakaoUser("11", "asdasd123@naver.com", true, "밴틀리", "벤", "틀리", "ㅁㄴㅇ", "ko");
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);

        System.out.println("responseEntity =================" + responseEntity);
//        return null;
        return responseEntity;
    }

    // 3. responseEntity에 담긴 JSON String을 역직렬화해 KakaoOAuthToken 객체에 담고 반환
    public KakaoOAuthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() ============== " + response.getBody());
        KakaoOAuthToken kakaoOAuthToken = objectMapper.readValue(response.getBody(),KakaoOAuthToken.class);

        return kakaoOAuthToken;

    }

    // 4. 다시 카카오로 3에서 받아온 액세스 토큰을 보내 카카오 사용자 정보를 받아온다.(email이 들어와야함!!!!!!!!!!!!!!)
    public ResponseEntity<String> requestUserInfo(KakaoOAuthToken oAuthToken) {
        String KAKAO_USERINFO_REQUEST_URL="https://kapi.kakao.com/v2/user/me";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        ResponseEntity<String> response = restTemplate.exchange(KAKAO_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);

        System.out.println("response.getBody() ==-=-=-=-=-=-======== " + response.getBody()); ///
        return response;
    }

    // 5. 카카오 유저 정보가 담긴 JSON 문자열을 파싱하여 KakaoUser 객체에 담기
    public KakaoUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException{
        KakaoUser kakaoUser = objectMapper.readValue(userInfoRes.getBody(),KakaoUser.class);
        return kakaoUser;
    }
}