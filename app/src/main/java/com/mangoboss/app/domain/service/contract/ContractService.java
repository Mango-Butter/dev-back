package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.common.constant.ContentType;
import com.mangoboss.app.common.constant.S3FileType;
import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.common.security.EncryptedFileDecoder;
import com.mangoboss.app.common.util.JsonConverter;
import com.mangoboss.app.common.util.PdfGenerator;
import com.mangoboss.app.common.util.S3FileManager;
import com.mangoboss.app.domain.repository.ContractRepository;
import com.mangoboss.app.dto.contract.request.ContractData;
import com.mangoboss.app.dto.contract.request.ContractTemplateData;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContractService {

    private final ContractRepository contractRepository;
    private final S3FileManager s3FileManager;
    private final ContractHtmlGenerator contractHtmlGenerator;

    private final PdfGenerator pdfGenerator;
    private final EncryptedFileDecoder encryptedFileDecoder;
    private final Clock clock;

    public String uploadSignature(final String signatureData) {
        final String signatureKey = s3FileManager.generateFileKey(S3FileType.SIGNATURE, ContentType.PNG.getExtension());

        final byte[] imageBytes = encryptedFileDecoder.decode(signatureData).fileBytes();

        s3FileManager.upload(imageBytes, signatureKey, ContentType.PNG.getMimeType());

        return signatureKey;
    }

    @Transactional
    public ContractEntity createContract(final Long staffId, final String bossSignatureKey, final ContractData contractData) {
        final byte[] pdfBytes = generateContractPdf(contractData, bossSignatureKey);

        final String fileKey = s3FileManager.generateFileKey(S3FileType.CONTRACT, ContentType.PDF.getExtension());

        s3FileManager.upload(pdfBytes, fileKey, ContentType.PDF.getMimeType());

        final String contractDataJson = convertToContractDataJson(contractData);

        final LocalDateTime now = LocalDateTime.now(clock);
        final ContractEntity contract = ContractEntity.create(staffId, fileKey, contractDataJson, now, bossSignatureKey, now);
        return contractRepository.save(contract);
    }

    @Transactional
    public ContractEntity signByStaff(final Long contractId, final String staffSignatureKey) {
        final ContractEntity contract = getContractById(contractId);

        final ContractData contractData = convertFromContractDataJson(contract.getContractDataJson());

        final byte[] pdfBytes = generateStaffSignedContractPdf(contractData, contract.getBossSignatureKey(), staffSignatureKey);
        final String existingFileKey = contract.getFileKey();
        s3FileManager.upload(pdfBytes, existingFileKey, ContentType.PDF.getMimeType());

        return contract.completeStaffSign(existingFileKey, staffSignatureKey, LocalDateTime.now(clock));
    }

    public ContractEntity getContractById(final Long contractId) {
        return contractRepository.getContractById(contractId);
    }

    public ContractData convertFromContractDataJson(final String contractDataJson) {
        return JsonConverter.fromJson(contractDataJson, ContractData.class);
    }

    public String convertToContractDataJson(final ContractData contractData) {
        return JsonConverter.toJson(contractData);
    }

    public ContractTemplateData convertFromContractTemplateJson(final String json) {
        return JsonConverter.fromJson(json, ContractTemplateData.class);
    }

    public String convertToContractTemplateJson(final ContractTemplateData data) {
        return JsonConverter.toJson(data);
    }

    public void validateContractBelongsToStaff(final Long contractStaffId, final Long staffId) {
        if (!contractStaffId.equals(staffId)) {
            throw new CustomException(CustomErrorInfo.CONTRACT_NOT_BELONG_TO_STAFF);
        }
    }

    public List<RegularGroupEntity> extractRegularGroupsFromContract(final ContractData contractData, final StaffEntity staff, final LocalDate limitedEndDate) {
        return contractData.workSchedules().stream()
                .map(ws -> RegularGroupEntity.create(
                        ws.dayOfWeek(),
                        ws.startTime(),
                        ws.endTime(),
                        contractData.contractStart(),
                        limitedEndDate,
                        staff
                ))
                .toList();
    }

    public List<ContractEntity> getContractsByStaffId(final Long staffId) {
        return contractRepository.findAllByStaffId(staffId);
    }

    private byte[] generateContractPdf(final ContractData contractData, final String bossSignatureKey) {
        final String bossSignatureBase64 = fetchSignatureBase64(bossSignatureKey);
        final String html = contractHtmlGenerator.generateHtmlWithBossSignature(contractData, bossSignatureBase64);
        return pdfGenerator.generatePdfFromHtml(html);
    }

    private byte[] generateStaffSignedContractPdf(final ContractData data, final String bossSignatureKey, final String staffSignatureKey) {
        final String bossSignatureBase64 = fetchSignatureBase64(bossSignatureKey);
        final String staffSignatureBase64 = fetchSignatureBase64(staffSignatureKey);
        final String html = contractHtmlGenerator.generateHtmlWithStaffSignature(data, bossSignatureBase64, staffSignatureBase64);
        return pdfGenerator.generatePdfFromHtml(html);
    }

    private String fetchSignatureBase64(final String signatureKey) {
        return s3FileManager.fetchAsBase64(signatureKey, ContentType.PNG.getMimeType());
    }
}