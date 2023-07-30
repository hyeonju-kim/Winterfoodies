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

import java.math.BigInteger;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverOauth implements SocialOauth {

    @Value("${sns.naver.client.id}")
    private String NAVER_SNS_CLIENT_ID;

    @Value("${sns.naver.redirect.uri}")
    private String NAVER_SNS_REDIRECT_URI;

    @Value("${sns.naver.client.secret}")
    private String NAVER_SNS_CLIENT_SECRET;

    @Value("${sns.naver.authorization.uri}")
    private String NAVER_SNS_AUTHORIZATION_URI;

    @Value("${sns.naver.token.uri}")
    private String NAVER_SNS_TOKEN_URI;

    @Value("${sns.naver.user.info.uri}")
    private String NAVER_SNS_USER_INFO_URI;

    @Value("${sns.naver.authorization.grantType}")
    private String NAVER_SNS_AUTH_TYPE;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    // 1. redirect Url 생성
    @Override
    public String getOauthRedirectURL() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.set("response_type", "code");
        queryParams.set("client_id", NAVER_SNS_CLIENT_ID);
        queryParams.set("redirect_uri", NAVER_SNS_REDIRECT_URI);
        queryParams.set("state", new BigInteger(130, new SecureRandom()).toString());

        return UriComponentsBuilder
                .fromUriString(NAVER_SNS_AUTHORIZATION_URI)
                .queryParams(queryParams)
                .encode().build().toString();
    }

    // 2. 코드 추가한 url 생성
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        try {
            queryParams.set("grant_type", NAVER_SNS_AUTH_TYPE);
            queryParams.set("client_id", NAVER_SNS_CLIENT_ID);
            queryParams.set("client_secret", NAVER_SNS_CLIENT_SECRET);
            queryParams.set("code", code);
            queryParams.set("state", new BigInteger(130, new SecureRandom()).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(NAVER_SNS_TOKEN_URI, queryParams, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        } else {
            throw new OAuthException("네이버 로그인에 실패하였습니다.");
        }
    }

    // 3. responseEntity에 담긴 JSON String을 역직렬화해 NaverOAuthToken 객체에 담고 반환
    public SocialOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() : {}", response.getBody());
        return objectMapper.readValue(response.getBody(), SocialOauthToken.class);

    }

    // 4. 다시 네이버로 3에서 받아온 액세스 토큰을 보내 네이버 사용자 정보를 받아온다.
    @Override
    public ResponseEntity<String> requestUserInfo(SocialOauthToken oAuthToken) {
        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + oAuthToken.getAccess_token());

        URI uri = UriComponentsBuilder
                .fromUriString(NAVER_SNS_USER_INFO_URI)
                .build().toUri();

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // 5. 네이버 유저 정보가 담긴 JSON 문자열을 파싱하여 GoogleUser 객체에 담기
    public SocailUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(Objects.requireNonNull(userInfoRes.getBody()));

        return objectMapper.readValue(jsonObject.get("response").toString(), SocailUser.class);
    }
}
