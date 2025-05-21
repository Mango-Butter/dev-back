package com.mangoboss.app.api.controller.payroll;

import lombok.NonNull;

import java.util.List;

public record ConfirmEstimatedPayrollRequest (
        @NonNull
        List<String> payrollKeys
){
}
