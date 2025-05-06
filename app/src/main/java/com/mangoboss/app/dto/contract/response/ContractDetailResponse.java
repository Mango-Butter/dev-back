package com.mangoboss.app.dto.contract.response;

import com.mangoboss.app.dto.contract.request.ContractData;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContractDetailResponse(
        ContractData contractData,
        ViewPreSignedUrlResponse bossSignature,
        ViewPreSignedUrlResponse staffSignature
) {
    public static ContractDetailResponse of(ContractData data,
                                            ViewPreSignedUrlResponse bossSigned, ViewPreSignedUrlResponse staffSigned) {
        return ContractDetailResponse.builder()
                .contractData(data)
                .bossSignature(bossSigned)
                .staffSignature(staffSigned)
                .build();
    }
}