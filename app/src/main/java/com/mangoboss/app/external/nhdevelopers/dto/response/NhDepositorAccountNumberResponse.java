package com.mangoboss.app.external.nhdevelopers.dto.response;


public record NhDepositorAccountNumberResponse (
        NhCommonPartHeaderResponse Header,
        String Bncd,
        String Acno,
        String Dpnm
){
}
