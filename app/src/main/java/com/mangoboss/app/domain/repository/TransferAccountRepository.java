package com.mangoboss.app.domain.repository;

import com.mangoboss.storage.payroll.TransferAccountEntity;

public interface TransferAccountRepository {
    TransferAccountEntity save(TransferAccountEntity transferAccountEntity);
}
