package com.mangoboss.app.dto.payroll.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TransferSummaryResponse(
        LocalDate transferDate,
        TransferSummaryStateForResponse isTransferred
) {
    public static TransferSummaryResponse of(final TransferSummaryStateForResponse state, final LocalDate transferDate) {
        return TransferSummaryResponse.builder()
                .transferDate(transferDate)
                .isTransferred(state)
                .build();
    }
}
