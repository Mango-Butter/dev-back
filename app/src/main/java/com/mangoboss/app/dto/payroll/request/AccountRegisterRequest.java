package com.mangoboss.app.dto.payroll.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AccountRegisterRequest(
        @NotBlank
        String bankName,

        @NotBlank
        @Pattern(regexp = "\\d+")
        String accountNumber
) {
}
