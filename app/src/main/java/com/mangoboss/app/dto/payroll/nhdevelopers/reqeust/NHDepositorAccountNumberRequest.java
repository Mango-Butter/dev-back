package com.mangoboss.app.dto.payroll.nhdevelopers.reqeust;

import lombok.Builder;

@Builder
public record NHDepositorAccountNumberRequest(
        NhCommonPartHeaderRequest Header,
        String Bncd,
        String Acno
) {

    public static NHDepositorAccountNumberRequest create(final NhCommonPartHeaderRequest header, final String bncd, final String acno) {
        return NHDepositorAccountNumberRequest.builder()
                .Header(header)
                .Bncd(bncd)
                .Acno(acno)
                .build();
    }
}
