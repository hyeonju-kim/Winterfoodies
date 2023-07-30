package com.winterfoodies.winterfoodies_project.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoOauth implements SocialOauth {

    @Value("${sns.kakao.client.id}")
    private String KAKAO_SNS_CLIENT_ID;

    @Value("${sns.kakao.redirect.uri}")
    private String KAKAO_SNS_CALLBACK_URL;

    @Value("${sns.kakao.authorization.grantType}")
    private String KAKAO_SNS_AUTH_TYPE;

    @Value("${sns.kakao.authorization.uri}")
    private String KAKAO_SNS_AUTHORIZATION_URI;

    @Value("${sns.kakao.token.uri}")
    private String KAKAO_SNS_TOKEN_URI;

    @Value("${sns.kakao.user.info.uri}")
    private String KAKAO_SNS_USER_INFO_URI;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    // 1. redirect Url 생성
    @Override
    public String getOauthRedirectURL() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.set("response_type", "code");
        queryParams.set("client_id", KAKAO_SNS_CLIENT_ID);
        queryParams.set("redirect_uri", KAKAO_SNS_CALLBACK_URL);

        return UriComponentsBuilder
                .fromUriString(KAKAO_SNS_AUTHORIZATION_URI)
                .queryParams(queryParams)
                .encode().build().toUriString();
    }

    // 2. 코드 추가한 url 생성
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.set("code", code);
        queryParams.set("client_id", KAKAO_SNS_CLIENT_ID);
        queryParams.set("redirect_uri", KAKAO_SNS_CALLBACK_URL);
        queryParams.set("grant_type", KAKAO_SNS_AUTH_TYPE);

        URI uri = UriComponentsBuilder
                .fromUriString(KAKAO_SNS_TOKEN_URI)
                .queryParams(queryParams)
                .encode().build().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // 3. responseEntity에 담긴 JSON String을 역직렬화해 KakaoOAuthToken 객체에 담고 반환
    public SocialOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() : {}", response.getBody());
        return objectMapper.readValue(response.getBody(),SocialOauthToken.class);
    }

    // 4. 다시 카카오로 3에서 받아온 액세스 토큰을 보내 카카오 사용자 정보를 받아온다.(email이 들어와야함!!!!!!!!!!!!!!)
    @Override
    public ResponseEntity<String> requestUserInfo(SocialOauthToken oAuthToken) {
        // Header에 AccessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

        URI uri = UriComponentsBuilder
                .fromUriString(KAKAO_SNS_USER_INFO_URI)
                .encode().build().toUri();

        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // 5. 카카오 유저 정보가 담긴 JSON 문자열을 파싱하여 KakaoUser 객체에 담기
    public SocailUser getUserInfo(ResponseEntity<String> userInfoRes) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(userInfoRes.getBody()));
        JSONObject accountObject = (JSONObject) jsonObject.get("kakao_account");
        JSONObject profileObject = (JSONObject) accountObject.get("profile");

        return SocailUser.builder()
                .id(String.valueOf((Long)jsonObject.get("id")))
                .name((String) profileObject.get("nickname"))
                .email((String) accountObject.get("email"))
                .build();
    }
}