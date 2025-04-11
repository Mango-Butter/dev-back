package com.mangoboss.app.auth;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.security.auth.login.LoginException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangoboss.app.dto.LoginRequest;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class OAuth {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    public String requestKakaoAccessToken(LoginRequest loginRequest) throws LoginException {
        String code = loginRequest.authorizationCode();

        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("Authorization code cannot be null or empty");
        }

        String url = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("grant_type", "authorization_code");
        parameters.add("client_id", kakaoClientId);
        parameters.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(parameters, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        if (response.getBody() == null) {
            throw new LoginException("카카오 토큰 응답이 비어 있습니다.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(response.getBody());
            return jsonNode.get("access_token").asText();
        } catch (Exception e) {
            throw new LoginException("카카오 토큰 파싱 실패");
        }
    }

    public UserInfoGetDto getUserInfoFromKakao(String accessToken) {
        String KAKAO_USERINFO_REQUEST_URL = "https://kapi.kakao.com/v2/user/me";

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

        if (response.getStatusCode().is2xxSuccessful()) {
            JsonNode body = response.getBody();
            if (body == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "카카오 사용자 정보 없음");
            }

            JsonNode kakaoAccount = body.get("kakao_account");

            JsonNode profile = kakaoAccount.has("profile") ? kakaoAccount.get("profile") : null;

            LocalDate birth = null;
            if (kakaoAccount.has("birthyear") && kakaoAccount.has("birthday")) {
                String birthStr = kakaoAccount.get("birthyear").asText() + kakaoAccount.get("birthday").asText(); // 20010326
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                birth = LocalDate.parse(birthStr, formatter); // ← LocalDate로 변환
            }

            String rawPhone = kakaoAccount.has("phone_number") ? kakaoAccount.get("phone_number").asText() : null;
            String formattedPhone = null;

            if (rawPhone != null && rawPhone.startsWith("+82")) {
                formattedPhone = rawPhone.replace("+82 ", "0");
            }

            return UserInfoGetDto.builder()
                .kakaoId(body.get("id").asLong())
                .email(kakaoAccount.has("email") ? kakaoAccount.get("email").asText() : null)
                .name(kakaoAccount.has("name") ? kakaoAccount.get("name").asText() : null)
                .picture(profile != null && profile.has("thumbnail_image_url") ? profile.get("thumbnail_image_url").asText() : null)
                .birth(birth)
                .phone(formattedPhone)
                .build();
        } else {
            log.error("카카오 사용자 정보 요청 실패: {}", response.getStatusCode());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "카카오 사용자 정보 요청 실패");
        }
    }


}
