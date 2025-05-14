package com.mangoboss.app.external.nhdevelopers.dto.reqeust;

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
