package com.mangoboss.app.dto.contract.response;

import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record ContractSummaryResponse(
        StaffSimpleResponse staffSimpleResponse,
        List<ContractSimpleResponse> contractSimpleResponses
) {
    public static ContractSummaryResponse of(StaffEntity staff, List<ContractEntity> contracts) {
        return ContractSummaryResponse.builder()
                .staffSimpleResponse(StaffSimpleResponse.fromEntity(staff))
                .contractSimpleResponses(
                        contracts.stream()
                                .map(ContractSimpleResponse::fromEntity)
                                .toList()
                )
                .build();
    }
}


