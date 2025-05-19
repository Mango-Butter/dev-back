package com.mangoboss.batch.external.nhdevelopers;

import com.mangoboss.batch.common.exception.CustomErrorInfo;
import com.mangoboss.batch.common.exception.CustomException;
import com.mangoboss.batch.external.nhdevelopers.dto.reqeust.*;
import com.mangoboss.batch.external.nhdevelopers.dto.response.DrawingTransferResponse;
import com.mangoboss.batch.external.nhdevelopers.dto.response.InquireTransactionHistoryResponse;
import com.mangoboss.batch.external.nhdevelopers.dto.response.ReceivedTransferResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Clock;
import java.util.List;

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
    @Value("${external.nh.api-svc-cd}")
    private String apiSvcCd;
    @Value("${external.nh.api-svc-cd-received}")
    private String apiSvcCdReceived;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        this.webClient = webClientBuilder
                .baseUrl(baseUrl)
                .build();
    }

    public DrawingTransferResponse drawingTransfer(final String finAcno,
                                                   final String tram,
                                                   final String dractotlt) {
        final CommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.DRAWING_TRANSFER, apiSvcCd, clock);
        final DrawingTransferRequest request = DrawingTransferRequest.create(requestHeader, finAcno, tram, dractotlt);
        DrawingTransferResponse response = webClient
                .post()
                .uri(nhUri(ApiName.DRAWING_TRANSFER.getName()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(DrawingTransferResponse.class)
                .block();
        if (response.Header().Rpcd().equals(SUCCESS_RPCD)) {
            return response;
        }
        throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
    }

    public ReceivedTransferResponse receivedTransfer(final String bncd,
                                                     final String acno,
                                                     final String tram,
                                                     final String dractOtlt) {
        final CommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.RECEIVED_TRANSFER_ACCOUNT_NUMBER, apiSvcCd, clock);
        final ReceivedTransferRequest request = ReceivedTransferRequest.create(requestHeader, bncd, acno, tram, dractOtlt);
        ReceivedTransferResponse response = webClient
                .post()
                .uri(nhUri(ApiName.RECEIVED_TRANSFER_ACCOUNT_NUMBER.getName()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ReceivedTransferResponse.class)
                .block();
        System.out.println(response);
        if (response.Header().Rpcd().equals(SUCCESS_RPCD)) {
            return response;
        }
        throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
    }

    public InquireTransactionHistoryResponse inquireTransaction(final String bncd,
                                                                final String acno,
                                                                final String insymd,
                                                                final String ineymd,
                                                                final String tsmDsnc,
                                                                final String lnsq,
                                                                final String dmcnt) {
        final CommonPartHeaderRequest requestHeader = headerFactory.create(ApiName.INQUIRE_TRANSACTION_HISTORY, apiSvcCdReceived, clock);
        final InquireTransactionHistoryRequest request = InquireTransactionHistoryRequest.create(
                requestHeader, bncd, acno, insymd, ineymd, tsmDsnc, lnsq, dmcnt
        );
        InquireTransactionHistoryResponse response = webClient
                .post()
                .uri(nhUri(ApiName.INQUIRE_TRANSACTION_HISTORY.getName()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(InquireTransactionHistoryResponse.class)
                .block();
        System.out.println(response);

        if (response.Header().Rpcd().equals(SUCCESS_RPCD)) {
            return response;
        }
        throw new CustomException(CustomErrorInfo.EXTERNAL_API_LOGICAL_FAILURE);
    }

    private String nhUri(String apiName) {
        return "/" + apiName + ".nh";
    }

}
