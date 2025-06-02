package com.mangoboss.app.api.controller.payroll;


import com.mangoboss.app.api.facade.payroll.BossPayrollFacade;

import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.request.ConfirmEstimatedPayrollRequest;
import com.mangoboss.app.dto.payroll.request.PayrollSettingRequest;
import com.mangoboss.app.dto.payroll.response.*;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boss/stores/{storeId}/payrolls")
@PreAuthorize("hasRole('BOSS')")
public class BossPayrollController {

    private final BossPayrollFacade bossPayrollFacade;

    @PostMapping("/account-verification")
    public AccountRegisterResponse verifyAndRegisterAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId, @RequestBody @Valid AccountRegisterRequest request) {
        final Long userId = userDetails.getUserId();
        return bossPayrollFacade.registerBossAccount(storeId, userId, request);
    }

    @PostMapping("/settings")
    public void updatePayrollSettings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long storeId, @RequestBody @Valid PayrollSettingRequest request) {
        final Long userId = userDetails.getUserId();
        bossPayrollFacade.updatePayrollSettings(storeId, userId, request);
    }

    @GetMapping("/settings")
    public PayrollSettingResponse getPayrollSettings(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return bossPayrollFacade.getPayrollSettings(storeId, userId);
    }

    @GetMapping("/staffs")
    public ListWrapperResponse<PayrollEstimatedWithStaffResponse> getEstimatedPayrolls(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                       @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossPayrollFacade.getEstimatedPayrolls(storeId, userId));
    }

    @PostMapping("/staffs")
    public void confirmEstimatedPayroll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable Long storeId,
                                        @RequestBody @Valid ConfirmEstimatedPayrollRequest request) {
        final Long userId = userDetails.getUserId();
        bossPayrollFacade.confirmEstimatedPayroll(storeId, userId, request);
    }

    @GetMapping("/confirm")
    public ListWrapperResponse<PayrollWithStaffResponse> getConfirmedPayrolls(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                              @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossPayrollFacade.getConfirmedPayrolls(storeId, userId));
    }

    @GetMapping("/{payrollId}")
    public PayrollResponse getPayroll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                      @PathVariable Long storeId, @PathVariable Long payrollId) {
        final Long userId = userDetails.getUserId();
        return bossPayrollFacade.getPayroll(storeId, payrollId, userId);
    }

    @DeleteMapping("/account")
    public void deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        bossPayrollFacade.deleteAccount(storeId, userId);
    }

    @GetMapping
    public ListWrapperResponse<PayrollWithStaffResponse> getPayrollsByMonth(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @PathVariable Long storeId, @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossPayrollFacade.getPayrollsByMonth(storeId, userId, yearMonth));
    }

    @GetMapping("/payslip/{payslipId}")
    public DownloadPreSignedUrlResponse getPayslipDownloadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable Long storeId, @PathVariable Long payslipId) {
        final Long userId = userDetails.getUserId();
        return bossPayrollFacade.getPayslipDownloadUrl(storeId, payslipId, userId);
    }

    @GetMapping("/staffs/{staffId}")
    public PayrollResponse getEstimatedPayrollForStaff(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long storeId,
            @PathVariable Long staffId,
            @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        final Long userId = userDetails.getUserId();
        return bossPayrollFacade.getEstimatedPayrollForStaff(storeId, staffId, userId, yearMonth);
    }
}
