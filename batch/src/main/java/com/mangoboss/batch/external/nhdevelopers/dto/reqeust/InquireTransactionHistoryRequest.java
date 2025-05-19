package com.mangoboss.batch.external.nhdevelopers.dto.reqeust;

import lombok.Builder;

@Builder
public record InquireTransactionHistoryRequest (
        CommonPartHeaderRequest Header,
        String Bncd,
        String Acno,
        String Insymd,
        String Ineymd,
        String TmsDsnc,
        String Lnsq,
        String PageNo,
        String Dmcnt
){
    public static InquireTransactionHistoryRequest create(final CommonPartHeaderRequest header,
                                                          final String bncd,
                                                          final String acno,
                                                          final String insymd,
                                                          final String ineymd,
                                                          final String tmsDsnc,
                                                          final String lnsq,
                                                          final String dmcnt) {
        return InquireTransactionHistoryRequest.builder()
                .Header(header)
                .Bncd(bncd)
                .Acno(acno)
                .Insymd(insymd)
                .Ineymd(ineymd)
                .TmsDsnc(tmsDsnc)
                .Lnsq(lnsq)
                .Dmcnt(dmcnt)
                .PageNo("1")
                .build();
    }
}
