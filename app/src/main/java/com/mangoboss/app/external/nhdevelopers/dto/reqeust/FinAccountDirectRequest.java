package com.mangoboss.app.external.nhdevelopers.dto.reqeust;

import lombok.Builder;

@Builder
public record FinAccountDirectRequest (
        CommonPartHeaderRequest Header,
        String DrtrRgyn,
        String BrdtBmo,
        String Bncd,
        String Acno
){
    public static FinAccountDirectRequest create(final CommonPartHeaderRequest header,final String brdtBmo, final String bncd, final String acno) {
        return FinAccountDirectRequest.builder()
                .Header(header)
                .DrtrRgyn("Y")
                .BrdtBmo(brdtBmo)
                .Bncd(bncd)
                .Acno(acno)
                .build();
    }
}
