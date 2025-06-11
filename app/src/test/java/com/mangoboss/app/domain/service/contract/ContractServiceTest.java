package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.security.EncryptedFileDecoder;
import com.mangoboss.app.common.util.DigestUtil;
import com.mangoboss.app.common.util.PdfGenerator;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.ContractRepository;
import com.mangoboss.app.dto.contract.request.ContractData;
import com.mangoboss.app.dto.contract.request.ContractTemplateData;
import com.mangoboss.app.dto.contract.request.WorkSchedule;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.metadata.ContentType;
import com.mangoboss.storage.metadata.S3FileType;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.staff.StaffEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private S3FileManager s3FileManager;

    @Mock
    private ContractHtmlGenerator contractHtmlGenerator;

    @Mock
    private PdfGenerator pdfGenerator;

    @Mock
    private EncryptedFileDecoder encryptedFileDecoder;

    @InjectMocks
    private ContractService contractService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-01T10:15:30Z"), ZoneId.of("UTC"));
        contractService = new ContractService(contractRepository, s3FileManager, contractHtmlGenerator, pdfGenerator, encryptedFileDecoder, fixedClock);
    }

    @Test
    void 사장님_서명을_업로드할_수_있다() {
        // given
        String signatureData = "encryptedSignatureData";
        String signatureKey = "test-signature-key";
        byte[] fileBytes = new byte[]{1, 2, 3};

        EncryptedFileDecoder.DecodedFile decodedFile = new EncryptedFileDecoder.DecodedFile("image/png", fileBytes);
        when(encryptedFileDecoder.decode(signatureData)).thenReturn(decodedFile);
        when(s3FileManager.generateFileKey(S3FileType.SIGNATURE, ContentType.PNG.getExtension())).thenReturn(signatureKey);

        // when
        String resultKey = contractService.uploadSignature(signatureData);

        // then
        verify(s3FileManager).upload(fileBytes, signatureKey, ContentType.PNG.getMimeType());
        assertThat(resultKey).isEqualTo(signatureKey);
    }

    @Test
    void 근로계약서를_생성할_수_있다() {
        // given
        Long staffId = 1L;
        String bossSignatureKey = "boss-signature-key";
        ContractData contractData = mock(ContractData.class);
        byte[] pdfBytes = new byte[]{1, 2, 3};
        String fileKey = "contract-file-key";

        when(contractHtmlGenerator.generateHtmlWithBossSignature(any(), any())).thenReturn("<html></html>");
        when(pdfGenerator.generatePdfFromHtml(any())).thenReturn(pdfBytes);
        when(s3FileManager.generateFileKey(S3FileType.CONTRACT, ContentType.PDF.getExtension())).thenReturn(fileKey);
        when(contractRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        String savedKey = contractService.createContract(staffId, bossSignatureKey, contractData).getFileKey();

        // then
        verify(s3FileManager).upload(pdfBytes, fileKey, ContentType.PDF.getMimeType());
        assertThat(savedKey).isEqualTo(fileKey);
    }

    @Test
    void 근로계약서를_알바생이_서명할_수_있다() {
        // given
        Long contractId = 1L;
        String staffSignatureKey = "staff-signature-key";
        String bossSignatureKey = "boss-signature-key";
        String fileKey = "contract-file-key";
        String contractDataJson = "{}";
        byte[] pdfBytes = new byte[]{1, 2, 3};

        ContractData contractData = mock(ContractData.class);
        ContractEntity contract = mock(ContractEntity.class);

        // mocking repository
        when(contractRepository.getContractById(contractId)).thenReturn(contract);
        when(contract.getContractDataJson()).thenReturn(contractDataJson);
        when(contract.getBossSignatureKey()).thenReturn(bossSignatureKey);
        when(contract.getFileKey()).thenReturn(fileKey);

        // mocking 내부 메서드
        ContractService spyService = spy(contractService);
        doReturn(contractData).when(spyService).convertFromContractDataJson(contractDataJson);
        doReturn(pdfBytes).when(spyService).generateStaffSignedContractPdf(contractData, bossSignatureKey, staffSignatureKey);
        doReturn(contract).when(contract).completeStaffSign(any(), any(), any(), any());

        // 실제 실행
        ContractEntity result = spyService.signByStaff(contractId, staffSignatureKey);

        // then
        verify(s3FileManager).upload(pdfBytes, fileKey, ContentType.PDF.getMimeType());
        verify(contract).completeStaffSign(eq(fileKey), eq(staffSignatureKey), any(), any());
        assertThat(result).isEqualTo(contract);
    }

    @Test
    void 근로계약서를_삭제할_수_있다() {
        // given
        Long contractId = 1L;
        String fileKey = "contract-file-key";

        ContractEntity contract = mock(ContractEntity.class);
        when(contractRepository.getContractById(contractId)).thenReturn(contract);
        when(contract.getFileKey()).thenReturn(fileKey);

        // when
        contractService.deleteContract(contractId);

        // then
        verify(s3FileManager).deleteFileFromPrivateBucket(fileKey);
        verify(contractRepository).delete(contract);
    }


    @Test
    void 근로계약서를_스태프ID로_조회할_수_있다() {
        Long staffId = 1L;
        List<ContractEntity> contracts = List.of(mock(ContractEntity.class));
        when(contractRepository.findAllByStaffId(staffId)).thenReturn(contracts);

        List<ContractEntity> result = contractService.getContractsByStaffId(staffId);

        assertThat(result).isEqualTo(contracts);
    }

    @Test
    void 근로계약서를_매장ID로_조회할_수_있다() {
        Long storeId = 1L;
        List<ContractEntity> contracts = List.of(mock(ContractEntity.class));
        when(contractRepository.findAllByStoreId(storeId)).thenReturn(contracts);

        List<ContractEntity> result = contractService.findAllByStoreId(storeId);

        assertThat(result).isEqualTo(contracts);
    }

    @Test
    void PDF_무결성_검증이_성공한다() {
        ContractEntity contract = mock(ContractEntity.class);
        byte[] pdfBytes = new byte[]{1, 2, 3};
        String hash = DigestUtil.sha256(pdfBytes);

        when(contract.getFileKey()).thenReturn("file-key");
        when(contract.getPdfHash()).thenReturn(hash);
        when(s3FileManager.fetchAsBytes("file-key")).thenReturn(pdfBytes);

        assertThatCode(() -> contractService.validatePdfIntegrity(contract))
                .doesNotThrowAnyException();
    }

    @Test
    void PDF_무결성_검증이_실패하면_예외를_던진다() {
        ContractEntity contract = mock(ContractEntity.class);
        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(contract.getFileKey()).thenReturn("file-key");
        when(contract.getPdfHash()).thenReturn("invalid-hash");
        when(s3FileManager.fetchAsBytes("file-key")).thenReturn(pdfBytes);

        assertThatThrownBy(() -> contractService.validatePdfIntegrity(contract))
                .isInstanceOf(CustomException.class);
    }

    @Test
    void 해당_계약서가_다른_알바생거면_예외를_던진다() {
        // given
        Long contractStaffId = 1L;
        Long staffId = 2L;

        // when & then
        assertThatThrownBy(() -> contractService.validateContractBelongsToStaff(contractStaffId, staffId))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.CONTRACT_NOT_BELONG_TO_STAFF.getMessage());
    }

    @Test
    void 해당_계약서가_해당_알바생거면_예외를_던지지_않는다() {
        // given
        Long contractStaffId = 1L;
        Long staffId = 1L;

        // when & then
        assertThatCode(() -> contractService.validateContractBelongsToStaff(contractStaffId, staffId))
                .doesNotThrowAnyException();
    }

    @Test
    void 계약서를_삭제할때_알바생_서명이_없으면_예외를_던지지_않는다() {
        // given
        ContractEntity contract = mock(ContractEntity.class);
        when(contract.getStaffSignatureKey()).thenReturn(null);

        // when & then
        assertThatCode(() -> contractService.validateContractNotSignedByStaff(contract))
                .doesNotThrowAnyException();
    }

    @Test
    void 계약서를_삭제할때_계약서에_알바생_서명이_있으면_예외를_던진다() {
        // given
        ContractEntity contract = mock(ContractEntity.class);
        when(contract.getStaffSignatureKey()).thenReturn("signed-key");

        // when & then
        assertThatThrownBy(() -> contractService.validateContractNotSignedByStaff(contract))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.STAFF_SIGNED_CONTRACT_CANNOT_BE_DELETED.getMessage());
    }

    @Test
    void JSON문자열을_ContractData로_변환할_수_있다() {
        // given
        String json = """
                    {
                      "contractStart": "2024-06-01",
                      "contractEnd": "2024-06-30",
                      "hourlyWage": 10000,
                      "workSchedules": []
                    }
                """;

        // when
        ContractData result = contractService.convertFromContractDataJson(json);

        // then
        assertThat(result.contractStart()).isEqualTo(LocalDate.of(2024, 6, 1));
        assertThat(result.contractEnd()).isEqualTo(LocalDate.of(2024, 6, 30));
        assertThat(result.hourlyWage()).isEqualTo(10000);
        assertThat(result.workSchedules()).isEmpty();
    }

    @Test
    void JSON문자열을_ContractTemplateData로_변환할_수_있다() {
        // given
        String json = """
                    {
                      "hourlyWage": 12000,
                      "workSchedules": []
                    }
                """;

        // when
        ContractTemplateData result = contractService.convertFromContractTemplateJson(json);

        // then
        assertThat(result.hourlyWage()).isEqualTo(12000);
        assertThat(result.workSchedules()).isEmpty();
    }

    @Test
    void 사장과_알바생_서명으로_계약서PDF를_생성할_수_있다() {
        // given
        ContractData contractData = mock(ContractData.class);
        String bossKey = "boss-signature-key";
        String staffKey = "staff-signature-key";

        String bossBase64 = "boss-signature";
        String staffBase64 = "staff-signature";
        String html = "<html>signed</html>";
        byte[] pdfBytes = new byte[]{1, 2, 3};

        when(s3FileManager.fetchAsBase64(bossKey, ContentType.PNG.getMimeType())).thenReturn(bossBase64);
        when(s3FileManager.fetchAsBase64(staffKey, ContentType.PNG.getMimeType())).thenReturn(staffBase64);
        when(contractHtmlGenerator.generateHtmlWithStaffSignature(contractData, bossBase64, staffBase64)).thenReturn(html);
        when(pdfGenerator.generatePdfFromHtml(html)).thenReturn(pdfBytes);

        // when
        byte[] result = contractService.generateStaffSignedContractPdf(contractData, bossKey, staffKey);

        // then
        assertThat(result).isEqualTo(pdfBytes);
    }

    @Test
    void 계약데이터에서_정기출근그룹리스트를_추출할_수_있다() {
        // given
        StaffEntity staff = mock(StaffEntity.class);
        LocalDate startDate = LocalDate.of(2024, 6, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);

        WorkSchedule schedule = new WorkSchedule(
                DayOfWeek.MONDAY,
                LocalTime.of(9, 0),
                LocalTime.of(18, 0)
        );
        ContractData data = ContractData.builder()
                .contractName("알바 근로계약서")
                .storeName("망고보스 신촌점")
                .staffName("김철수")
                .contractStart(LocalDate.of(2024, 6, 1))
                .contractEnd(LocalDate.of(2024, 6, 30))
                .bossName("홍사장")
                .storeAddress("서울시 마포구")
                .duty("서빙 및 매장 관리")
                .workSchedules(List.of(schedule))
                .hourlyWage(10000)
                .businessNumber("123-45-67890")
                .staffPhone("010-1234-5678")
                .build();

        // when
        List<RegularGroupEntity> result = contractService.extractRegularGroupsFromContract(data, staff, endDate);

        // then
        assertThat(result).hasSize(1);
        RegularGroupEntity group = result.get(0);
        assertThat(group.getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);
        assertThat(group.getStartTime()).isEqualTo(LocalTime.of(9, 0));
        assertThat(group.getEndTime()).isEqualTo(LocalTime.of(18, 0));
        assertThat(group.getStartDate()).isEqualTo(startDate);
        assertThat(group.getEndDate()).isEqualTo(endDate);
    }
}