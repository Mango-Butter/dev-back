package com.mangoboss.batch.external.nhdevelopers.dto.reqeust;


import lombok.Builder;

@Builder
public record ReceivedTransferRequest(
        CommonPartHeaderRequest Header,
        String Bncd,
        String Acno,
        String Tram,
        String DractOtlt
) {
    public static ReceivedTransferRequest create(final CommonPartHeaderRequest header, final String bncd,
                                                 final String acno, final String tram, final String dractOtlt) {
        return ReceivedTransferRequest.builder()
                .Header(header)
                .Bncd(bncd)
                .Acno(acno)
                .Tram(tram)
                .DractOtlt(dractOtlt)
                .build();
    }
}
