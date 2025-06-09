package com.mangoboss.batch.billing.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangoboss.batch.common.exception.CustomErrorInfo;
import com.mangoboss.batch.common.exception.CustomException;
import com.mangoboss.storage.subscription.PlanType;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.annotation.PostConstruct;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentClient {

    @Value("${external.toss.payment.secret-key}")
    private String secretKey;

    private static final String TOSS_PAYMENTS_BASE_URL = "https://api.tosspayments.com";

    private final WebClient.Builder webClientBuilder;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder
                .baseUrl(TOSS_PAYMENTS_BASE_URL)
                .build();
    }

    public void approveBilling(String billingKey, String customerKey, PlanType planType, UserEntity boss, String orderId) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("customerKey", customerKey);
        requestBody.put("amount", planType.getAmount());
        requestBody.put("orderId", orderId);
        requestBody.put("orderName", planType.getOrderName());
        requestBody.put("customerEmail", boss.getEmail());
        requestBody.put("customerName", boss.getName());
        requestBody.put("taxFreeAmount", 0);

        String responseBody = webClient.post()
                .uri("/v1/billing/{billingKey}", billingKey)
                .header("Authorization", "Basic " + encodeSecretKey())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            Map<String, Object> responseMap = new ObjectMapper().readValue(responseBody, Map.class);
            String status = (String) responseMap.get("status");

            if (!"DONE".equals(status)) {
                throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
            }

            log.info("[결제성공] orderId={}, amount={}", orderId, planType.getAmount());
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
        }
    }

    private String encodeSecretKey() {
        return Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
    }
}