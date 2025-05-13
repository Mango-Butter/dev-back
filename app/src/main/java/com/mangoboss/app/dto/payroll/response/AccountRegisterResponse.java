package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.TransferAccountEntity;
import lombok.Builder;

@Builder
public record AccountRegisterResponse(
        String bankName,
        String accountHolder,
        String accountNumber
) {
    public static AccountRegisterResponse fromEntity(final TransferAccountEntity transferAccount) {
        return AccountRegisterResponse.builder()
                .bankName(transferAccount.getBankCode().getName())
                .accountHolder(transferAccount.getAccountHolder())
                .accountNumber(transferAccount.getAccountNumber())
                .build();
    }
}
