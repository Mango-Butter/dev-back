package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.ContractRepository;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.contract.ContractJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepository {
    private final ContractJpaRepository contractJpaRepository;

    @Override
    public ContractEntity save(final ContractEntity contract) {
        return contractJpaRepository.save(contract);
    }

    @Override
    public ContractEntity getContractById(final Long contractId) {
        return contractJpaRepository.findById(contractId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.CONTRACT_NOT_FOUND));
    }

    @Override
    public List<ContractEntity> findAllByStaffId(final Long staffId) {
        return contractJpaRepository.findAllByStaffId(staffId);
    }
}
