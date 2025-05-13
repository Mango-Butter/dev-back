package com.mangoboss.app.api.controller.payroll;


import com.mangoboss.app.api.facade.payroll.BossPayrollFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.payroll.request.AccountRegisterRequest;
import com.mangoboss.app.dto.payroll.response.AccountRegisterResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boss/stores/{storeId}/payroll")
public class BossPayrollController {

    private final BossPayrollFacade bossPayrollFacade;

    @PostMapping("/account-verification")
    public AccountRegisterResponse verifyAndRegisterAccount(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @PathVariable Long storeId, @RequestBody @Valid AccountRegisterRequest request){
        final Long userId = userDetails.getUserId();
        return bossPayrollFacade.registerBossAccount(storeId,userId,request);
    }
}
