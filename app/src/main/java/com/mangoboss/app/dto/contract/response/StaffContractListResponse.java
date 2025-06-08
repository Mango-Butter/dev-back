package com.mangoboss.app.dto.contract.response;

import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.contract.ContractStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record StaffContractListResponse(
        Long contractId,
        LocalDateTime modifiedAt,
        ContractStatus status
) {
    public static StaffContractListResponse fromEntity(final ContractEntity contract) {
        return StaffContractListResponse.builder()
                .contractId(contract.getId())
                .modifiedAt(contract.getModifiedAt())
                .status(contract.getStatus())
                .build();
    }
}
