package com.mangoboss.app.external.nhdevelopers.dto.reqeust;

import lombok.Builder;

@Builder
public record CheckFinAccountRequest(
        CommonPartHeaderRequest Header,
        String Rgno,
        String BrdtBrno
) {
    public static CheckFinAccountRequest create(final CommonPartHeaderRequest header, final String rgno, final String brdtBrno) {
        return CheckFinAccountRequest.builder()
                .Header(header)
                .Rgno(rgno)
                .BrdtBrno(brdtBrno)
                .build();
    }
}
