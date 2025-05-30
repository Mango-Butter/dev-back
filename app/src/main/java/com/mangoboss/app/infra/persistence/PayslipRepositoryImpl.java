package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.domain.repository.PayslipRepository;
import com.mangoboss.storage.payroll.PayslipJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PayslipRepositoryImpl implements PayslipRepository {
    private final PayslipJpaRepository payslipJpaRepository;
}
