package com.mangoboss.app.dto.contract.response;

import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.contract.ContractStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContractSimpleResponse(
        Long contractId,
        LocalDateTime modifiedAt,
        LocalDateTime bossSignedAt,
        LocalDateTime staffSignedAt,
        ContractStatus status
) {
    public static ContractSimpleResponse fromEntity(final ContractEntity contract) {
        return ContractSimpleResponse.builder()
                .contractId(contract.getId())
                .modifiedAt(contract.getModifiedAt())
                .bossSignedAt(contract.getBossSignedAt())
                .staffSignedAt(contract.getStaffSignedAt())
                .status(contract.getStatus())
                .build();
    }
}

