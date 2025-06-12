package com.mangoboss.app.domain.service.payroll;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.util.S3PreSignedUrlManager;
import com.mangoboss.app.domain.repository.PayslipRepository;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.storage.payroll.PayslipEntity;
import com.mangoboss.storage.payroll.PayslipState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PayslipServiceTest {
    @Mock
    private PayslipRepository payslipRepository;

    @Mock
    private S3PreSignedUrlManager s3PreSignedUrlManager;

    @InjectMocks
    private PayslipService payslipService;

    private final LocalDateTime fixedNow = LocalDateTime.of(2025, 1, 1, 9, 0);
    private final Clock fixedClock = Clock.fixed(fixedNow.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

    @Test
    void payrollId로_급여명세서를_조회할_수_있다() {
        // given
        Long payrollId = 1L;
        PayslipEntity payslip = mock(PayslipEntity.class);
        when(payslipRepository.findByPayrollId(payrollId)).thenReturn(Optional.of(payslip));

        // when
        PayslipEntity result = payslipService.getPayslipByPayrollId(payrollId);

        // then
        assertThat(result).isEqualTo(payslip);
    }

    @Test
    void 급여명세서가_없을_경우_null을_반환한다() {
        // given
        Long payrollId = 1L;
        when(payslipRepository.findByPayrollId(payrollId)).thenReturn(Optional.empty());

        // when
        PayslipEntity result = payslipService.getPayslipByPayrollId(payrollId);

        // then
        assertThat(result).isNull();
    }

    @Test
    void 급여명세서_다운로드_URL을_반환한다() {
        // given
        Long payslipId = 1L;
        PayslipEntity payslip = mock(PayslipEntity.class);
        String fileKey = "files/payslip1.pdf";
        DownloadPreSignedUrlResponse response = new DownloadPreSignedUrlResponse("https://example.com", fixedNow.plusMinutes(10));

        when(payslipRepository.getById(payslipId)).thenReturn(payslip);
        when(payslip.getPayslipState()).thenReturn(PayslipState.COMPLETED);
        when(payslip.getFileKey()).thenReturn(fileKey);
        when(s3PreSignedUrlManager.generateDownloadPreSignedUrl(fileKey)).thenReturn(response);

        // when
        DownloadPreSignedUrlResponse result = payslipService.getPayslipDownloadUrl(payslipId);

        // then
        assertThat(result).isEqualTo(response);
    }

    @Test
    void 급여명세서_다운로드_URL을_반환할때_payroll이_COMPLETED가_아니면_에러를_던진다() {
        // given
        Long payslipId = 1L;
        PayslipEntity payslip = mock(PayslipEntity.class);

        when(payslipRepository.getById(payslipId)).thenReturn(payslip);
        when(payslip.getPayslipState()).thenReturn(PayslipState.PENDING);

        // when & then
        assertThatThrownBy(() -> payslipService.getPayslipDownloadUrl(payslipId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(CustomErrorInfo.PAYSLIP_PDF_NOT_FOUND.getMessage());
    }

    @Test
    void 급여명세서의_주인인지_검증한다() {
        // given
        Long payslipId = 1L;
        Long userId = 123L;
        PayslipEntity payslip = mock(PayslipEntity.class);

        when(payslipRepository.getByIdAndStaffId(payslipId, userId)).thenReturn(payslip);

        // when
        PayslipEntity result = payslipService.verifyPayslipOwner(payslipId, userId);

        // then
        assertThat(result).isEqualTo(payslip);
    }
}