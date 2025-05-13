package com.mangoboss.storage.payroll;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum BankCode {
    NH("농협은행", "011"),
    NH_SANGHO("농협상호금융", "012");

    private final String displayName;
    private final String code;

    BankCode(final String name, final String code) {
        this.displayName = name;
        this.code = code;
    }

    public static Optional<BankCode> findCodeByName(final String name) {
        return Arrays.stream(BankCode.values())
                .filter(b -> b.displayName.equals(name))
                .findFirst();
    }
}
