package com.mangoboss.app.external.tosspayment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class TossPaymentClient {

    @Value("${external.toss.payment.secret-key}")
    private String secretKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> issueBillingKey(String customerKey, String authKey) {
        String url = "https://api.tosspayments.com/v1/billing/authorizations/issue";

        Map<String, String> body = Map.of(
                "customerKey", customerKey,
                "authKey", authKey
        );

        log.info("TossPaymentClient.issueBillingKey called with customerKey={}, authKey={}", customerKey, authKey);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, createAuthHeaders());
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
        return response.getBody();
    }

    private HttpHeaders createAuthHeaders() {
        String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
