package com.mangoboss.batch.transfer.external.nhdevelopers.dto.response;

public record DrawingTransferResponse (
        CommonPartHeaderResponse Header,
        String FinAcno,
        String RgsnYmd
){
}
