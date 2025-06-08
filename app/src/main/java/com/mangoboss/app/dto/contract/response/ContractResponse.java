package com.mangoboss.app.dto.contract.response;

import com.mangoboss.storage.contract.ContractEntity;
import lombok.Builder;

@Builder
public record ContractResponse(
        Long contractId
) {
    public static ContractResponse fromEntity(final ContractEntity contract) {
        return ContractResponse.builder()
                .contractId(contract.getId())
                .build();
    }
}