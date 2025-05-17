package com.mangoboss.app.external.nhdevelopers.dto.response;

public record CheckFinAccountResponse (
        CommonPartHeaderResponse Header,
        String FinAcno,
        String RgsnYmd
){
}
