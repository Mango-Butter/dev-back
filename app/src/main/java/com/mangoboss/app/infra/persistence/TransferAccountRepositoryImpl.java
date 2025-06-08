package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.TransferAccountRepository;
import com.mangoboss.storage.payroll.TransferAccountEntity;
import com.mangoboss.storage.payroll.TransferAccountJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TransferAccountRepositoryImpl implements TransferAccountRepository {
    private final TransferAccountJpaRepository transferAccountJpaRepository;

    @Override
    public TransferAccountEntity save(final TransferAccountEntity transferAccountEntity) {
        return transferAccountJpaRepository.save(transferAccountEntity);
    }

    @Override
    public void deleteById(final Long id) {
        transferAccountJpaRepository.deleteById(id);
    }
}
