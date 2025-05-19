package com.mangoboss.batch.external.nhdevelopers.dto.response;

public record DrawingTransferResponse (
        CommonPartHeaderResponse Header,
        String FinAcno,
        String RgsnYmd
){
}
