package com.mangoboss.app.external.nhdevelopers.dto.response;

import lombok.Builder;

public record FinAccountDirectResponse(
        CommonPartHeaderResponse Header,
        String Rgno
) {
}
