package com.mangoboss.app.dto.contract.request;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ContractTemplateData(

        LocalDate contractStart,

        LocalDate contractEnd,

        String duty,

        List<WorkSchedule> workSchedules,

        Integer hourlyWage

) {}