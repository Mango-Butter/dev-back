package com.mangoboss.app.dto.payroll.nhdevelopers.response;


public record NhDepositorAccountNumberResponse (
        NhCommonPartHeaderResponse Header,
        String Bncd,
        String Acno,
        String Dpnm
){
}
