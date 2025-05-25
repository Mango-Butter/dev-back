package com.mangoboss.storage.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PayslipJpaRepository extends JpaRepository<PayslipEntity, Long> {
}
