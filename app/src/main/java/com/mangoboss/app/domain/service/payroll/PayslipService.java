package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.S3PreSignedUrlManager;
import com.mangoboss.app.domain.repository.PayslipRepository;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PayslipService {
    private final PayslipRepository payslipRepository;
    private final S3PreSignedUrlManager preSignedUrlManager;

    public Optional<PayslipEntity> getPayslipByPayrollId(final Long payrollId) {
        return payslipRepository.findByPayrollId(payrollId);
    }

    public DownloadPreSignedUrlResponse getPayslipDownloadUrl(final Long payslipId) {
        PayslipEntity payslip = payslipRepository.getById(payslipId);
        if(payslip.getPayslipState().equals(PayslipState.COMPLETED)) {
            String key = payslip.getFileKey();
            return preSignedUrlManager.generateDownloadPreSignedUrl(key);
        }
        throw new CustomException(CustomErrorInfo.PAYSLIP_PDF_NOT_FOUND);
    }
}
