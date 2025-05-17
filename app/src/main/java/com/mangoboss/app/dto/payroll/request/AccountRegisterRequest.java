package com.mangoboss.app.dto.payroll.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountRegisterRequest(
        @NotBlank
        String bankName,

        @NotBlank
        @Pattern(regexp = "\\d+")
        String accountNumber,

        @NotBlank
        @Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])$")
        String birthdate
) {
}
