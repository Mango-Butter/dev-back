package com.mangoboss.app.api.controller.payroll;

import com.mangoboss.app.api.facade.payroll.StaffPayrollFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.payroll.response.PayrollDetailResponse;
import com.mangoboss.app.dto.payroll.response.PayrollResponse;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/stores/{storeId}/payrolls")
@PreAuthorize("hasRole('STAFF')")
public class StaffPayrollController {
    private final StaffPayrollFacade staffPayrollFacade;

    @GetMapping
    public PayrollResponse getPayrollDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                            @PathVariable Long storeId,
                                            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final Long userId = userDetails.getUserId();
        return staffPayrollFacade.getPayrollDetail(storeId, userId, yearMonth);
    }

    @GetMapping("/payslip/{payslipId}")
    public DownloadPreSignedUrlResponse getPayrollDownloadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @PathVariable Long storeId,
                                                         @PathVariable Long payslipId) {
        final Long userId = userDetails.getUserId();
        return staffPayrollFacade.getPayrollDownloadUrl(storeId, userId, payslipId);
    }
}
