package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.payroll.nhdevelopers.reqeust.ApiName;
import com.mangoboss.app.dto.payroll.nhdevelopers.reqeust.NhDepositorAccountNumberRequest;
import com.mangoboss.app.dto.payroll.nhdevelopers.reqeust.NhCommonPartHeaderRequest;
import com.mangoboss.app.dto.payroll.nhdevelopers.reqeust.NhHeaderFactory;
import com.mangoboss.app.dto.payroll.nhdevelopers.response.NhDepositorAccountNumberResponse;
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

    public String getVerifyAccountHolder(final String bankCode, final String accountNumber) {
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
            if(!response.Header().Rsms().equals("정상처리 되었습니다.")){
                throw new CustomException(CustomErrorInfo.INVALID_ACCOUNT);
            }
            return response.Dpnm();
        }catch (WebClientResponseException e){
            throw new CustomException(CustomErrorInfo.EXTERNAL_API_EXCEPTION);
        }
    }
}
