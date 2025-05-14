package com.mangoboss.app.external.nhdevelopers.dto.response;

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
