package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.PayrollRepository;
import com.mangoboss.storage.payroll.PayrollEntity;
import com.mangoboss.storage.payroll.PayrollJpaRepository;
import com.mangoboss.storage.payroll.TransferState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PayrollRepositoryImpl implements PayrollRepository {
    private final PayrollJpaRepository payrollJpaRepository;

    @Override
    public PayrollEntity save(final PayrollEntity payroll) {
        return payrollJpaRepository.save(payroll);
    }

    @Override
    public List<PayrollEntity> saveAll(final List<PayrollEntity> payrolls) {
        return payrollJpaRepository.saveAll(payrolls);
    }

    @Override
    public void deleteAllByStoreIdAndMonth(final Long storeId, final LocalDate month) {
        payrollJpaRepository.deleteAllByStoreIdAndMonth(storeId, month);
    }

    @Override
    public boolean isNotTransferPending(final Long storeId, final LocalDate month) {
        return payrollJpaRepository.existsByStoreIdAndMonthAndTransferStateNot(storeId, month, TransferState.PENDING);
    }

    @Override
    public List<PayrollEntity> findAllByStoreIdAndMonth(final Long storeId, final LocalDate month){
        return payrollJpaRepository.getAllByStoreIdAndMonth(storeId, month);
    }

    @Override
    public List<PayrollEntity> findAllByStoreIdAndMonthBetween(final Long storeId, final LocalDate start, final LocalDate end){
        return payrollJpaRepository.findAllByStoreIdAndMonthBetween(storeId, start, end);
    }

    @Override
    public boolean isTransferStarted(final Long storeId, final LocalDate start, final LocalDate end) {
        return payrollJpaRepository.existsByStoreIdAndMonthBetweenAndTransferStateNot(storeId, start, end, TransferState.PENDING);
    }

    @Override
    public PayrollEntity getById(final Long id) {
        return payrollJpaRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.PAYROLL_NOT_FOUND));
    }

    @Override
    public PayrollEntity getByIdAndStoreId(final Long id, final Long storeId) {
        return payrollJpaRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.PAYROLL_NOT_FOUND));
    }

    @Override
    public PayrollEntity getByStaffIdAndMonthBetween(final Long staffId, final LocalDate start, final LocalDate end) {
        return payrollJpaRepository.findByStaffIdAndMonthBetween(staffId, start, end)
                .orElse(null);
    }
}
