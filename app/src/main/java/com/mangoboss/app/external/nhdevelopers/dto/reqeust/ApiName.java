package com.mangoboss.app.external.nhdevelopers.dto.reqeust;

import lombok.Getter;

@Getter
public enum ApiName {
    InquireDepositorAccountNumber("InquireDepositorAccountNumber"),
    OpenFinAccountDirect("OpenFinAccountDirect"),
    CheckOpenFinAccountDirect("CheckOpenFinAccountDirect");

    private final String name;

    ApiName(final String name) {
        this.name = name;
    }
}
