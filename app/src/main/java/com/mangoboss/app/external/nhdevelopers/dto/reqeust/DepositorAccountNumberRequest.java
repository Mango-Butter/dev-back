package com.mangoboss.app.external.nhdevelopers.dto.reqeust;

import lombok.Builder;

@Builder
public record DepositorAccountNumberRequest(
        CommonPartHeaderRequest Header,
        String Bncd,
        String Acno
) {

    public static DepositorAccountNumberRequest create(final CommonPartHeaderRequest header, final String bncd, final String acno) {
        return DepositorAccountNumberRequest.builder()
                .Header(header)
                .Bncd(bncd)
                .Acno(acno)
                .build();
    }
}
