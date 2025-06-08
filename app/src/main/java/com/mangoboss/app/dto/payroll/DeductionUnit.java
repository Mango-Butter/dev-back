package com.mangoboss.app.dto.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
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
        return Arrays.stream(DeductionUnit.values())
                .filter(d -> d.value.equals(value))
                .findFirst().orElseThrow(()-> new CustomException(CustomErrorInfo.UNMAPPED_DEDUCTION_UNIT_EXCEPTION));
    }
}
