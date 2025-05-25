package com.mangoboss.batch.transfer.external.nhdevelopers.dto.response;

import java.util.List;

public record InquireTransactionHistoryResponse(
        List<REC> REC,
        String CtnDataYn,
        CommonPartHeaderResponse Header,
        String TotCnt,
        String Iqtcnt
) {
    public record REC(
            String AftBlnc,
            String TrnsAfAcntBlncSmblCd,
            String BnprCntn,
            String Txtm,
            String Tuno,
            String Trdd,
            String Smr,
            String Ccyn,
            String MnrcDrotDsnc,
            String Tram,
            String HiniCd,
            String HnbrCd
    ) {
    }
}
