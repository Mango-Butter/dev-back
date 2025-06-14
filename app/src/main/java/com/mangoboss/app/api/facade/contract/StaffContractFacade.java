package com.mangoboss.app.api.facade.contract;

import com.mangoboss.app.common.util.*;
import com.mangoboss.app.domain.service.contract.ContractService;
import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.dto.contract.request.ContractData;
import com.mangoboss.app.dto.contract.request.ContractSignRequest;
import com.mangoboss.app.dto.contract.request.SignatureUploadRequest;
import com.mangoboss.app.dto.contract.request.WorkSchedule;
import com.mangoboss.app.dto.contract.response.*;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import com.mangoboss.storage.contract.ContractEntity;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StaffContractFacade {

    private final ContractService contractService;
    private final StaffService staffService;
    private final ScheduleService scheduleService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;
    private final Clock clock;

    public SignatureUploadResponse uploadSignature(final Long storeId, final Long userId, final SignatureUploadRequest request) {
        staffService.getVerifiedStaff(userId, storeId);
        final String signatureKey = contractService.uploadSignature(request.signatureData());
        return SignatureUploadResponse.of(signatureKey);
    }

    public ContractResponse signContract(final Long storeId, final Long contractId, final Long userId, final ContractSignRequest contractSignRequest) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);

        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());
        final ContractEntity signedContract = contractService.signByStaff(contractId, contractSignRequest.staffSignatureKey());

        final ContractData contractData = contractService.convertFromContractDataJson(contract.getContractDataJson());
        staffService.updateHourlyWage(staff, contractData.hourlyWage());
        applyRegularSchedulesFromContractData(contractData, staff, storeId);

        return ContractResponse.fromEntity(signedContract);
    }

    public ViewPreSignedUrlResponse getContractViewUrl(final Long storeId, final Long contractId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());

        contractService.validatePdfIntegrity(contract);

        return s3PreSignedUrlManager.generateViewPreSignedUrl(contract.getFileKey());
    }

    public DownloadPreSignedUrlResponse getContractDownloadUrl(final Long storeId, final Long contractId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());

        contractService.validatePdfIntegrity(contract);

        return s3PreSignedUrlManager.generateDownloadPreSignedUrl(contract.getFileKey());
    }

    public ContractDetailResponse getContractDetail(final Long storeId, final Long contractId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final ContractEntity contract = contractService.getContractById(contractId);
        contractService.validateContractBelongsToStaff(contract.getStaffId(), staff.getId());

        final ContractData contractData = contractService.convertFromContractDataJson(contract.getContractDataJson());

        final ViewPreSignedUrlResponse bossSigned = s3PreSignedUrlManager.generateViewPreSignedUrl(contract.getBossSignatureKey());

        final ViewPreSignedUrlResponse staffSigned = contract.getStaffSignatureKey() != null
                ? s3PreSignedUrlManager.generateViewPreSignedUrl(contract.getStaffSignatureKey())
                : ViewPreSignedUrlResponse.builder().url("").expiresAt(null).build();

        return ContractDetailResponse.of(contractData, bossSigned, staffSigned);
    }

    public List<StaffContractListResponse> getMyContracts(final Long storeId, final Long userId) {
        final StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final List<ContractEntity> contracts = contractService.getContractsByStaffId(staff.getId());
        return contracts.stream()
                .map(StaffContractListResponse::fromEntity)
                .toList();
    }

    private void applyRegularSchedulesFromContractData(final ContractData contractData, final StaffEntity staff, final Long storeId) {
        final LocalDate contractStart = contractData.contractStart();
        final LocalDate adjustedStartDate = contractStart.isBefore(LocalDate.now(clock).plusDays(1))
                ? LocalDate.now(clock).plusDays(1)
                : contractStart;
        final LocalDate originalEndDate = contractData.contractEnd();
        final LocalDate limitedEndDate = originalEndDate.isAfter(adjustedStartDate.plusYears(1))
                ? adjustedStartDate.plusYears(1)
                : originalEndDate;


        for (WorkSchedule workSchedule : contractData.workSchedules()) {
            scheduleService.validateDate(adjustedStartDate, limitedEndDate, workSchedule.startTime(), workSchedule.endTime());
        }

        final List<RegularGroupEntity> regularGroups =
                contractService.extractRegularGroupsFromContract(contractData, staff, limitedEndDate);

        scheduleService.createRegularGroupAndSchedules(regularGroups, storeId);
    }
}