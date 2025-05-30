package com.mangoboss.storage.payroll.projection;

import com.mangoboss.storage.payroll.PayrollEntity;

public interface PayrollWithPayslipProjection {
    PayrollEntity getPayroll();
    String getPayslipFileKey();
}
