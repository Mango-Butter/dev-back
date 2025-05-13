package com.mangoboss.app.dto.payroll.request;

import lombok.Getter;

@Getter
public enum DeductionUnit {
    ZERO_MIN(0), FIVE_MIN(5), TEN_MIN(10), THIRTY_MIN(30);

    private final Integer value;

    DeductionUnit(Integer value) {
        this.value = value;
    }
}
