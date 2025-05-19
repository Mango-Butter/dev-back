package com.mangoboss.batch.external.nhdevelopers.dto.reqeust;

import lombok.Builder;

@Builder
public record CommonPartHeaderRequest(
        String ApiNm,
        String Tsymd,
        String Trtm,
        String Iscd,
        String FintechApsno,
        String ApiSvcCd,
        String IsTuno,
        String AccessToken
) {
}
