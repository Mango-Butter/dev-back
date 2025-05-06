package com.mangoboss.app.dto.contract.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ContractDataInput(
        @NotBlank
        String contractName,

        @NotBlank
        String duty,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate contractStart,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate contractEnd,

        @NotNull
        List<WorkSchedule> workSchedules,

        @NotNull
        Integer hourlyWage
) {
}