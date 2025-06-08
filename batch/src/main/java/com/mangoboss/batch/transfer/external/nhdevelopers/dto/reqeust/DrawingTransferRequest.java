package com.mangoboss.batch.transfer.external.nhdevelopers.dto.reqeust;

import lombok.Builder;

@Builder
public record DrawingTransferRequest(
        CommonPartHeaderRequest Header,
        String FinAcno,
        String Tram,
        String DractOtlt
) {
    public static DrawingTransferRequest create(
            final CommonPartHeaderRequest header,
            final String finAcno,
            final String tram,
            final String dractOtlt
    ) {
        return DrawingTransferRequest.builder()
                .Header(header)
                .FinAcno(finAcno)
                .Tram(tram)
                .DractOtlt(dractOtlt)
                .build();
    }
}
