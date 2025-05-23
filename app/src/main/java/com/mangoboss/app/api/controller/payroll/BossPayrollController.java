package com.mangoboss.app.api.controller.payroll;


import com.mangoboss.app.api.facade.payroll.BossPayrollFacade;

import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.request.ConfirmEstimatedPayrollRequest;
import com.mangoboss.app.dto.payroll.request.PayrollSettingRequest;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import com.mangoboss.app.dto.payroll.response.PayrollEstimatedResponse;
import com.mangoboss.app.dto.payroll.response.PayrollSettingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boss/stores/{storeId}/payrolls")
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
    public ListWrapperResponse<PayrollEstimatedResponse> getEstimatedPayrollsForStaffs(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                       @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossPayrollFacade.getEstimatedPayrollsForStaffs(storeId, userId));
    }

    @PostMapping("/staffs")
    public void confirmEstimatedPayroll(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @PathVariable Long storeId,
                                        @RequestBody @Valid ConfirmEstimatedPayrollRequest request) {
        final Long userId = userDetails.getUserId();
        bossPayrollFacade.confirmEstimatedPayroll(storeId, userId, request);
    }

    @GetMapping("/staffs/confirm")
    public ListWrapperResponse<PayrollEstimatedResponse> getConfirmedPayrolls(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                              @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossPayrollFacade.getConfirmedPayroll(storeId, userId));
    }

    @DeleteMapping("/account")
    public void deleteAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long storeId){
        final Long userId = userDetails.getUserId();
        bossPayrollFacade.deleteAccount(storeId, userId);
    }
}
