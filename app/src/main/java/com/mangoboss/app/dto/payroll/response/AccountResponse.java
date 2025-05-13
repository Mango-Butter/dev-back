package com.mangoboss.app.dto.payroll.response;

import com.mangoboss.storage.payroll.TransferAccountEntity;
import lombok.Builder;

@Builder
public record AccountResponse (
        String bankName,
        String accountNumber,
        String accountHolder
){
    public static AccountResponse fromEntity(final TransferAccountEntity transferAccount) {
        return AccountResponse.builder()
                .bankName(transferAccount.getBankCode().getDisplayName())
                .accountNumber(transferAccount.getAccountNumber())
                .accountHolder(transferAccount.getAccountHolder())
                .build();
    }
}
