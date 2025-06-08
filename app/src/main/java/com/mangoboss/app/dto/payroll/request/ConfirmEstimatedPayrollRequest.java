package com.mangoboss.app.dto.payroll.request;

import lombok.NonNull;

import java.util.List;

public record ConfirmEstimatedPayrollRequest (
        @NonNull
        List<String> payrollKeys
){
}
