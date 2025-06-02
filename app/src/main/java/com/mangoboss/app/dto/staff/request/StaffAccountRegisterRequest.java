package com.mangoboss.app.dto.staff.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record StaffAccountRegisterRequest(
        @NotBlank
        String bankName,

        @NotBlank
        @Pattern(regexp = "\\d+")
        String accountNumber
) {
}
