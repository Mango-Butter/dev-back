package com.mangoboss.app.api.facade.contract;

import com.mangoboss.app.common.util.*;
import com.mangoboss.app.domain.service.contract.ContractService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.domain.service.user.UserService;
import com.mangoboss.app.dto.contract.request.*;
import com.mangoboss.app.dto.contract.response.*;

import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BossContractFacade {

    private final ContractService contractService;
    private final StoreService storeService;
    private final StaffService staffService;
    private final UserService userService;
    private final S3FileManager s3FileManager;

    public SignatureUploadResponse uploadSignature(final Long storeId, final Long bossId, final SignatureUploadRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        final String signatureKey = contractService.uploadSignature(request.signatureData());
        return SignatureUploadResponse.of(signatureKey);
    }

    public ContractResponse createContract(final Long storeId, final Long bossId, final ContractCreateRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        final UserEntity boss = userService.getUserById(bossId);
        final StoreEntity store = storeService.getStoreById(storeId);
        final StaffEntity staff = staffService.getStaffBelongsToStore(storeId, request.staffId());

        final ContractData contractData = ContractData.of(request.contractDataInput(), boss, store, staff);
        final ContractEntity contract = contractService.create(request.staffId(), request.bossSignatureKey(), contractData);
        return ContractResponse.fromEntity(contract);
    }

    public ViewPreSignedUrlResponse getContractViewUrl(final Long storeId, final Long bossId, final Long contractId) {
        storeService.isBossOfStore(storeId, bossId);
        final ContractEntity contract = contractService.getContractById(contractId);
        return s3FileManager.generateViewPreSignedUrl(contract.getFileKey());
    }

    public DownloadPreSignedUrlResponse getContractDownloadUrl(final Long storeId, final Long bossId, final Long contractId) {
        storeService.isBossOfStore(storeId, bossId);
        final ContractEntity contract = contractService.getContractById(contractId);
        return s3FileManager.generateDownloadPreSignedUrl(contract.getFileKey());
    }

    public ContractDetailResponse getContractDetail(final Long storeId, final Long bossId, final Long contractId) {
        storeService.isBossOfStore(storeId, bossId);
        final ContractEntity contract = contractService.getContractById(contractId);

        final ContractData contractData = contractService.convertFromJson(contract.getContractDataJson());

        final ViewPreSignedUrlResponse bossSigned = s3FileManager.generateViewPreSignedUrl(contract.getBossSignatureKey());

        final ViewPreSignedUrlResponse staffSigned = contract.getStaffSignatureKey() != null
                ? s3FileManager.generateViewPreSignedUrl(contract.getStaffSignatureKey())
                : ViewPreSignedUrlResponse.builder().url("").expiresAt(null).build();

        return ContractDetailResponse.of(contractData, bossSigned, staffSigned);
    }
}