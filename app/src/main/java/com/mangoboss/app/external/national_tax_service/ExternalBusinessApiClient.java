package com.mangoboss.app.external.national_tax_service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class ExternalBusinessApiClient {

    @Value("${external.business-api.key}")
    private String businessApiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.odcloud.kr/api/nts-businessman/v1")
            .build();

    // 사업자번호 유효성 확인 API (상태조회)
    public boolean checkBusinessNumberValid(String businessNumber) {
        try {
            Map<String, Object> body = Map.of("b_no", List.of(businessNumber));

            JsonNode response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/status")
                            .queryParam("serviceKey", businessApiKey)
                            .queryParam("returnType", "JSON")
                            .build())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block(); // 동기 방식 (Spring WebFlux 안 쓸 경우 안전)
            log.info(response.toString());

            JsonNode statusNode = response.path("data").get(0).path("b_stt");

            return statusNode != null && !statusNode.asText().isBlank();

        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.BUSINESS_API_FAILED);
        }
    }
}