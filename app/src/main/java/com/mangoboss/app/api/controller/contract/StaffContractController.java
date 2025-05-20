package com.mangoboss.app.api.controller.contract;

import com.mangoboss.app.api.facade.contract.StaffContractFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.contract.request.ContractSignRequest;
import com.mangoboss.app.dto.contract.request.SignatureUploadRequest;
import com.mangoboss.app.dto.contract.response.*;
import com.mangoboss.app.dto.s3.response.DownloadPreSignedUrlResponse;
import com.mangoboss.app.dto.s3.response.ViewPreSignedUrlResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/staff/stores/{storeId}/contracts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffContractController {

    private final StaffContractFacade staffContractFacade;

    @PostMapping("/signature-upload")
    public SignatureUploadResponse uploadSignature(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable Long storeId,
                                                   @RequestBody @Valid SignatureUploadRequest request) {
        final Long userId = userDetails.getUserId();
        return staffContractFacade.uploadSignature(storeId, userId, request);
    }

    @PostMapping("/{contractId}/sign")
    public ContractResponse signContract(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long storeId,
                                         @PathVariable Long contractId,
                                         @RequestBody @Valid ContractSignRequest request) {
        final Long userId = userDetails.getUserId();
        return staffContractFacade.signContract(storeId, contractId, userId, request);
    }

    @GetMapping("/{contractId}/view-url")
    public ViewPreSignedUrlResponse viewContractFile(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long storeId,
                                                     @PathVariable Long contractId) {
        final Long userId = userDetails.getUserId();
        return staffContractFacade.getContractViewUrl(storeId, contractId, userId);
    }

    @GetMapping("/{contractId}/download-url")
    public DownloadPreSignedUrlResponse getContractDownloadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long storeId,
                                                               @PathVariable Long contractId) {
        final Long userId = userDetails.getUserId();
        return staffContractFacade.getContractDownloadUrl(storeId, contractId, userId);
    }

    @GetMapping("/{contractId}")
    public ContractDetailResponse getContractDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long storeId,
                                                    @PathVariable Long contractId) {
        final Long userId = userDetails.getUserId();
        return staffContractFacade.getContractDetail(storeId, contractId, userId);
    }

    @GetMapping
    public ListWrapperResponse<StaffContractListResponse> getMyContracts(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                         @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffContractFacade.getMyContracts(storeId, userId));
    }
}