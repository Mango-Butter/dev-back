package com.mangoboss.app.external.nhdevelopers;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.external.nhdevelopers.dto.reqeust.*;
import com.mangoboss.app.external.nhdevelopers.dto.response.CheckFinAccountResponse;
import com.mangoboss.app.external.nhdevelopers.dto.response.DepositorAccountNumberResponse;
import com.mangoboss.app.external.nhdevelopers.dto.response.FinAccountDirectResponse;
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
    private static final String SUCCESS_RPCD = "00000";

    private final WebClient.Builder webClientBuilder;
    private final HeaderFactory headerFactory;
    private final Clock clock;

    @Value("${external.nh.base-url}")
    private String baseUrl;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public DepositorAccountNumberResponse getVerifyAccountHolder(final String bankCode, final String accountNumber) {
        final CommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.InquireDepositorAccountNumber, clock);
        final DepositorAccountNumberRequest request = DepositorAccountNumberRequest.create(requestHeader, bankCode, accountNumber);
        try {
            DepositorAccountNumberResponse response = webClient
                    .post()
                    .uri(nhUri(ApiName.InquireDepositorAccountNumber.getName()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(DepositorAccountNumberResponse.class)
                    .block();
            if (response.Header().Rpcd().equals(SUCCESS_RPCD)) {
                return response;
            }
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
        } catch (WebClientResponseException e) {
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_EXCEPTION);
        }
    }

    public FinAccountDirectResponse finAccountDirect(final String bankCode, final String accountNumber, final String birthDate) {
        final CommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.OpenFinAccountDirect, clock);
        final FinAccountDirectRequest request = FinAccountDirectRequest.create(requestHeader, bankCode, accountNumber, birthDate);
        try {
            FinAccountDirectResponse response = webClient
                    .post()
                    .uri(nhUri(ApiName.OpenFinAccountDirect.getName()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(FinAccountDirectResponse.class)
                    .block();
            if (response.Header().Rpcd().equals(SUCCESS_RPCD)) {
                return response;
            }
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
        } catch (WebClientResponseException e) {
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_EXCEPTION);
        }
    }

    public CheckFinAccountResponse checkFinAccount(final String rgno, final String birthDate) {
        final CommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.CheckOpenFinAccountDirect, clock);
        final CheckFinAccountRequest request = CheckFinAccountRequest.create(requestHeader, rgno, birthDate);
        try {
            CheckFinAccountResponse response = webClient
                    .post()
                    .uri(nhUri(ApiName.CheckOpenFinAccountDirect.getName()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(CheckFinAccountResponse.class)
                    .block();
            if (response.Header().Rpcd().equals(SUCCESS_RPCD)) {
                return response;
            }
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
        } catch (WebClientResponseException e) {
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_EXCEPTION);
        }
    }

    private String nhUri(String apiName) {
        return "/" + apiName + ".nh";
    }
}
