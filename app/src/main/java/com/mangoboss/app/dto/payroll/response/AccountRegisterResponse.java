package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.Builder;

@Builder
public record AccountRegisterResponse(
        String bankName,
        String accountHolder,
        String accountNumber
) {
    public static AccountRegisterResponse fromEntity(final TransferAccountEntity transferAccount) {
        return AccountRegisterResponse.builder()
                .bankName(transferAccount.getBankCode().getDisplayName())
                .accountHolder(transferAccount.getAccountHolder())
                .accountNumber(transferAccount.getAccountNumber())
                .build();
    }

    public static AccountRegisterResponse fromStaffEntity(final StaffEntity staff) {
        if(staff.getBankCode()==null || staff.getAccountNumber()==null) {
            return AccountRegisterResponse.builder()
                    .bankName(null)
                    .accountHolder(null)
                    .accountNumber(null)
                    .build();
        }
        return AccountRegisterResponse.builder()
                .bankName(staff.getBankCode().getDisplayName())
                .accountHolder(staff.getName())
                .accountNumber(staff.getAccountNumber())
                .build();
    }
}
