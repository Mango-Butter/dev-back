package com.mangoboss.app.api.facade.contract;

import com.mangoboss.app.common.util.*;
import com.mangoboss.app.domain.service.contract.ContractService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.contract.request.ContractData;
import com.mangoboss.app.dto.contract.request.ContractSignRequest;
import com.mangoboss.app.dto.contract.request.SignatureUploadRequest;
import com.mangoboss.app.dto.contract.response.*;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffContractFacade {

    private final ContractService contractService;
    private final StaffService staffService;
    private final S3FileManager s3FileManager;

    public SignatureUploadResponse uploadSignature(final Long storeId, final Long staffId, final SignatureUploadRequest request) {
        staffService.getStaffBelongsToStore(storeId, staffId);
        final String signatureKey = contractService.uploadSignature(request.signatureData());
        return SignatureUploadResponse.of(signatureKey);
    }

    public ContractResponse signContract(final Long storeId, final Long contractId, final Long staffId, final ContractSignRequest contractSignRequest) {
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, staffId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());
        final ContractEntity signedContract = contractService.signByStaff(contractId, contractSignRequest.staffSignatureKey());
        return ContractResponse.fromEntity(signedContract);
    }

    public ViewPreSignedUrlResponse getContractViewUrl(final Long storeId, final Long contractId, final Long staffId) {
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, staffId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());
        return s3FileManager.generateViewPreSignedUrl(contract.getFileKey());
    }

    public DownloadPreSignedUrlResponse getContractDownloadUrl(final Long storeId, final Long contractId, final Long staffId) {
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, staffId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());
        return s3FileManager.generateDownloadPreSignedUrl(contract.getFileKey());
    }

    public ContractDetailResponse getContractDetail(final Long storeId, final Long contractId, final Long staffId) {
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, staffId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());

        final ContractData contractData = contractService.convertFromJson(contract.getContractDataJson());

        final ViewPreSignedUrlResponse bossSigned = s3FileManager.generateViewPreSignedUrl(contract.getBossSignatureKey());

        final ViewPreSignedUrlResponse staffSigned = contract.getStaffSignatureKey() != null
                ? s3FileManager.generateViewPreSignedUrl(contract.getStaffSignatureKey())
                : ViewPreSignedUrlResponse.builder().url("").expiresAt(null).build();

        return ContractDetailResponse.of(contractData, bossSigned, staffSigned);
    }
}