package com.mangoboss.app.dto.payroll.nhdevelopers.response;

import lombok.Builder;

@Builder
public record NhCommonPartHeaderResponse(
        String ApiNm,
        String Trtm,
        String Iscd,
        String FintechApsno,
        String ApiSvcCd,
        String IsTuno,
        String Rpcd,
        String Rsms
) {
}
