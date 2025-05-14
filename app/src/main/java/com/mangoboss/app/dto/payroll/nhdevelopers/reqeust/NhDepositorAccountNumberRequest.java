package com.mangoboss.app.dto.payroll.nhdevelopers.reqeust;

import lombok.Builder;

@Builder
public record NhDepositorAccountNumberRequest(
        NhCommonPartHeaderRequest Header,
        String Bncd,
        String Acno
) {

    public static NhDepositorAccountNumberRequest create(final NhCommonPartHeaderRequest header, final String bncd, final String acno) {
        return NhDepositorAccountNumberRequest.builder()
                .Header(header)
                .Bncd(bncd)
                .Acno(acno)
                .build();
    }
}
