package com.mangoboss.app.dto.payroll.nhdevelopers.reqeust;

import lombok.Getter;

@Getter
public enum ApiName {
    InquireDepositorAccountNumber("InquireDepositorAccountNumber");

    private final String name;

    ApiName(final String name) {
        this.name = name;
    }
}
