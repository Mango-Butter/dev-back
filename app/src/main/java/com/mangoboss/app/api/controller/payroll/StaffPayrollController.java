package com.mangoboss.app.api.controller.payroll;

import com.mangoboss.app.api.facade.payroll.StaffPayrollFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/staff/stores/{storeId}/payrolls")
@PreAuthorize("hasRole('STAFF')")
public class StaffPayrollController {
    private final StaffPayrollFacade staffPayrollFacade;

//    @GetMapping
//    public PayrollDetailResponse getPayrollDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
//                                                  @PathVariable Long storeId,
//                                                  @RequestParam("month") @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
//        final Long userId = userDetails.getUserId();
//        return staffPayrollFacade.getPayrollDetail(storeId, userId, yearMonth);
//    }
}
