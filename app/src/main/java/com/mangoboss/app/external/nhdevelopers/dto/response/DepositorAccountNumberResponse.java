package com.mangoboss.app.external.nhdevelopers.dto.response;


public record DepositorAccountNumberResponse(
        CommonPartHeaderResponse Header,
        String Bncd,
        String Acno,
        String Dpnm
){
}
