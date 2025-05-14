package com.mangoboss.app.external.nhdevelopers;

import com.mangoboss.app.common.exception.ExternalApiServerException;
import com.mangoboss.app.external.nhdevelopers.dto.reqeust.ApiName;
import com.mangoboss.app.external.nhdevelopers.dto.reqeust.NhDepositorAccountNumberRequest;
import com.mangoboss.app.external.nhdevelopers.dto.reqeust.NhCommonPartHeaderRequest;
import com.mangoboss.app.external.nhdevelopers.dto.reqeust.NhHeaderFactory;
import com.mangoboss.app.external.nhdevelopers.dto.response.NhDepositorAccountNumberResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Clock;

@Slf4j
@Component
@RequiredArgsConstructor
public class NhDevelopersClient {

    @Value("${external.nh.base-url}")
    private String baseUrl;

    private final WebClient.Builder webClientBuilder;
    private final NhHeaderFactory headerFactory;
    private final Clock clock;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public NhDepositorAccountNumberResponse getVerifyAccountHolder(final String bankCode, final String accountNumber) {
        final NhCommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.InquireDepositorAccountNumber, clock);
        final NhDepositorAccountNumberRequest request = NhDepositorAccountNumberRequest.create(requestHeader, bankCode, accountNumber);
        try {
            final NhDepositorAccountNumberResponse response = webClient
                    .post()
                    .uri("/InquireDepositorAccountNumber.nh")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(NhDepositorAccountNumberResponse.class)
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            log.warn("NH API 통신 오류", e);
            throw new ExternalApiServerException("NH API 요청 실패", e);
        }
    }
}
