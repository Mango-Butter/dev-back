package com.mangoboss.app.dto.contract.response;

import com.mangoboss.storage.contract.ContractEntity;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StaffContractListResponse(
        Long contractId,
        LocalDateTime createdAt,
        boolean isSigned
) {
    public static StaffContractListResponse fromEntity(final ContractEntity contract) {
        return StaffContractListResponse.builder()
                .contractId(contract.getId())
                .createdAt(contract.getCreatedAt())
                .isSigned(contract.getStaffSignatureKey() != null)
                .build();
    }
}