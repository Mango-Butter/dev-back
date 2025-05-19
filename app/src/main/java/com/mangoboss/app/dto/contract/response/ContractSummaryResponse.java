package com.mangoboss.app.dto.contract.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.contract.ContractStatus;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ContractSummaryResponse(
        Long contractId,
        LocalDateTime modifiedAt,
        ContractStatus status,
        StaffSimpleResponse staff
) {
    public static ContractSummaryResponse fromStaffOnly(final StaffEntity staff) {
        return ContractSummaryResponse.builder()
                .contractId(null)
                .modifiedAt(null)
                .status(ContractStatus.NOT_CREATED)
                .staff(StaffSimpleResponse.fromEntity(staff))
                .build();
    }

    public static ContractSummaryResponse fromEntity(final ContractEntity contract, final StaffEntity staff) {
        return ContractSummaryResponse.builder()
                .contractId(contract.getId())
                .modifiedAt(contract.getModifiedAt())
                .status(contract.getStatus())
                .staff(StaffSimpleResponse.fromEntity(staff))
                .build();
    }
}


