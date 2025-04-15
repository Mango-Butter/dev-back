package com.mangoboss.app.common.security;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.KakaoUserInfo;
import com.mangoboss.app.dto.LoginRequest;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class OAuth {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    private static final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private static final String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";
    private static final DateTimeFormatter BIRTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public String requestKakaoAccessToken(final LoginRequest loginRequest) {
        String code = loginRequest.authorizationCode();

        if (code == null || code.isBlank()) {
            throw new CustomException(CustomErrorInfo.AUTHORIZATION_CODE_MISSING);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientId);
        parameters.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(KAKAO_TOKEN_URL, request, String.class);

        if (response.getBody() == null) {
            throw new CustomException(CustomErrorInfo.KAKAO_TOKEN_RESPONSE_EMPTY);
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.KAKAO_TOKEN_PARSING_FAILED);
        }
    }

    public KakaoUserInfo getUserInfoFromKakao(final String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<JsonNode> response = restTemplate.exchange(
            KAKAO_USERINFO_REQUEST_URL,
            HttpMethod.GET,
            entity,
            JsonNode.class
        );

        if (!response.getStatusCode().is2xxSuccessful()) {
            log.error("카카오 사용자 정보 요청 실패: {}", response.getStatusCode());
            throw new CustomException(CustomErrorInfo.KAKAO_USER_INFO_REQUEST_FAILED);
        }

        JsonNode body = response.getBody();
        if (body == null) {
            throw new CustomException(CustomErrorInfo.KAKAO_USER_INFO_NOT_FOUND);
        }

        JsonNode kakaoAccount = body.get("kakao_account");
        JsonNode profile = kakaoAccount.has("profile") ? kakaoAccount.get("profile") : null;

        LocalDate birth = parseBirth(kakaoAccount);
        String phone = formatPhone(kakaoAccount);
        String email = kakaoAccount.has("email") ? kakaoAccount.get("email").asText() : null;
        String name = kakaoAccount.has("name") ? kakaoAccount.get("name").asText() : null;
        String thumbnail = profile != null && profile.has("thumbnail_image_url") ? profile.get("thumbnail_image_url").asText() : null;

        return KakaoUserInfo.create(
            body.get("id").asLong(),
            email,
            name,
            thumbnail,
            birth,
            phone
        );
    }

    private LocalDate parseBirth(final JsonNode kakaoAccount) {
        if (kakaoAccount != null && kakaoAccount.has("birthyear") && kakaoAccount.has("birthday")) {
            final String birthStr = kakaoAccount.get("birthyear").asText() + kakaoAccount.get("birthday").asText();
            return LocalDate.parse(birthStr, BIRTH_FORMATTER);
        }
        return null;
    }

    private String formatPhone(final JsonNode kakaoAccount) {
        final String rawPhone = kakaoAccount.has("phone_number") ? kakaoAccount.get("phone_number").asText() : null;
        if (rawPhone != null && rawPhone.startsWith("+82")) {
            return rawPhone.replace("+82 ", "0");
        }
        return rawPhone;
    }

}
