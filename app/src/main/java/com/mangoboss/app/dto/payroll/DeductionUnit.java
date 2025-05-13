package com.mangoboss.app.dto.payroll;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum DeductionUnit {
    ZERO_MIN(0), FIVE_MIN(5), TEN_MIN(10), THIRTY_MIN(30);

    private final Integer value;

    DeductionUnit(Integer value) {
        this.value = value;
    }

    public static DeductionUnit getDeductionUnit(final Integer value) {
        final Optional<DeductionUnit> deductionUnit = Arrays.stream(DeductionUnit.values())
                .filter(d -> d.value.equals(value))
                .findFirst();
        return deductionUnit.orElse(null);
    }
}
