package com.mangoboss.storage.payroll;

import lombok.Getter;

@Getter
public enum WithholdingType {
    NONE("공제없음",0.000),
    INCOME_TAX("원천징수",0.033),
    SOCIAL_INSURANCE("4대보험",0.094);

    private final String label;
    private final double rate;

    WithholdingType(String label, double rate) {
        this.label = label;
        this.rate = rate;
    }
}
