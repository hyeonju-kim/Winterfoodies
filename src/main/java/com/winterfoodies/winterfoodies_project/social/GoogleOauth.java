package com.winterfoodies.winterfoodies_project.social;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOauth implements SocialOauth {
    @Value("${sns.google.url}")
    private String GOOGLE_SNS_BASE_URL;

    @Value("${sns.google.client.id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${sns.google.callback.url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${sns.google.client.secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${sns.google.token.url}")
    private String GOOGLE_SNS_TOKEN_BASE_URL;

    private final ObjectMapper objectMapper;

    private final RestTemplate restTemplate;

    // 1. redirect Url 생성
    @Override
    public String getOauthRedirectURL() {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.set("scope", "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile"); // profile로 설정하면 사용자가 설정한 범위의 정보만 가져오고, email로 설정하면 email만 가져올 수 있음.
        queryParams.set("response_type", "code");
        queryParams.set("client_id", GOOGLE_SNS_CLIENT_ID);
        queryParams.set("redirect_uri", GOOGLE_SNS_CALLBACK_URL);

        return UriComponentsBuilder
                .fromUriString(GOOGLE_SNS_BASE_URL)
                .queryParams(queryParams)
                .encode().build().toString();
    }

    // 2. 코드 추가한 url 생성
    @Override
    public ResponseEntity<String> requestAccessToken(String code) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.set("code", code);
        queryParams.set("client_id", GOOGLE_SNS_CLIENT_ID);
        queryParams.set("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        queryParams.set("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        queryParams.set("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_SNS_TOKEN_BASE_URL, queryParams, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        } else {
            throw new OAuthException("구글 로그인에 실패하였습니다.");
        }
    }

    // 3. responseEntity에 담긴 JSON String을 역직렬화해 GoogleOAuthToken 객체에 담고 반환
    public SocialOauthToken getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        log.info("response.getBody() : {}", response.getBody());
        return objectMapper.readValue(response.getBody(),SocialOauthToken.class);

    }

    // 4. 다시 구글로 3에서 받아온 액세스 토큰을 보내 구글 사용자 정보를 받아온다.(email이 들어와야함!!!!!!!!!!!!!!)
    @Override
    public ResponseEntity<String> requestUserInfo(SocialOauthToken oAuthToken) {
        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());

        URI uri = UriComponentsBuilder
                .fromUriString("https://www.googleapis.com/oauth2/v1/userinfo")
                .build().toUri();

        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<>(headers), String.class);
    }

    // 5. 구글 유저 정보가 담긴 JSON 문자열을 파싱하여 GoogleUser 객체에 담기
    @Override
    public SocailUser getUserInfo(ResponseEntity<String> userInfoRes) throws JsonProcessingException {
        return objectMapper.readValue(userInfoRes.getBody(),SocailUser.class);
    }
}
