package com.mangoboss.app.dto.payroll.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record TransferSummaryResponse(
        LocalDate transferDate,
        TransferSummaryStateForResponse isTransferred
) {
    public static TransferSummaryResponse of(final TransferSummaryStateForResponse state, final Integer transferDate, final LocalDate today) {
        if( transferDate == null) {
            return TransferSummaryResponse.builder()
                    .transferDate(null)
                    .isTransferred(state)
                    .build();
        }
        return TransferSummaryResponse.builder()
                .transferDate(today.withDayOfMonth(transferDate))
                .isTransferred(state)
                .build();
    }

    public static TransferSummaryResponse of(final TransferSummaryStateForResponse state, final LocalDate transferDate) {
        return TransferSummaryResponse.builder()
                .transferDate(transferDate)
                .isTransferred(state)
                .build();
    }
}
