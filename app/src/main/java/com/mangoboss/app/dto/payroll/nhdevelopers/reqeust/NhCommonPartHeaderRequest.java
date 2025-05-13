package com.mangoboss.app.dto.payroll.nhdevelopers.reqeust;

import lombok.Builder;

@Builder
public record NhCommonPartHeaderRequest(
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
