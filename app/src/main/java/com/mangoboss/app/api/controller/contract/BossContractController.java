package com.mangoboss.app.api.controller.contract;

import com.mangoboss.app.api.facade.contract.BossContractFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.contract.request.ContractCreateRequest;
import com.mangoboss.app.dto.contract.request.ContractTemplateCreateRequest;
import com.mangoboss.app.dto.contract.request.ContractTemplateUpdateRequest;
import com.mangoboss.app.dto.contract.request.SignatureUploadRequest;
import com.mangoboss.app.dto.contract.response.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boss/stores/{storeId}/contracts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BOSS')")
public class BossContractController {

    private final BossContractFacade bossContractFacade;

    @PostMapping("/signature-upload")
    public SignatureUploadResponse uploadSignature(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                   @PathVariable Long storeId,
                                                   @RequestBody @Valid SignatureUploadRequest request) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.uploadSignature(storeId, userId, request);
    }

    @PostMapping("")
    public ContractResponse createContract(@AuthenticationPrincipal CustomUserDetails userDetails,
                                           @PathVariable Long storeId,
                                           @RequestBody @Valid ContractCreateRequest request) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.createContract(storeId, userId, request);
    }

    @GetMapping("/{contractId}/view-url")
    public ViewPreSignedUrlResponse getContractViewUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                       @PathVariable Long storeId,
                                                       @PathVariable Long contractId) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.getContractViewUrl(storeId, userId, contractId);
    }

    @GetMapping("/{contractId}/download-url")
    public DownloadPreSignedUrlResponse getContractDownloadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                               @PathVariable Long storeId,
                                                               @PathVariable Long contractId) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.getContractDownloadUrl(storeId, userId, contractId);
    }

    @GetMapping("/{contractId}")
    public ContractDetailResponse getContractDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                    @PathVariable Long storeId,
                                                    @PathVariable Long contractId) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.getContractDetail(storeId, userId, contractId);
    }

    @PostMapping("/templates")
    public ContractTemplateResponse createContractTemplate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable Long storeId,
                                                           @RequestBody @Valid ContractTemplateCreateRequest request) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.createContractTemplate(storeId, userId, request);
    }

    @GetMapping("/templates")
    public ListWrapperResponse<ContractTemplateResponse> getAllContractTemplates(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                 @PathVariable Long storeId) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(bossContractFacade.getAllContractTemplates(storeId, userId));
    }

    @GetMapping("/templates/{templateId}")
    public ContractTemplateDetailResponse getContractTemplate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                              @PathVariable Long storeId,
                                                              @PathVariable Long templateId) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.getContractTemplate(storeId, userId, templateId);
    }

    @PostMapping("/templates/{templateId}")
    public ContractTemplateResponse updateContractTemplate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @PathVariable Long storeId,
                                                           @PathVariable Long templateId,
                                                           @RequestBody @Valid ContractTemplateUpdateRequest request) {
        final Long userId = userDetails.getUserId();
        return bossContractFacade.updateContractTemplate(storeId, userId, templateId, request);
    }

    @DeleteMapping("/templates/{templateId}")
    public void deleteContractTemplate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                       @PathVariable Long storeId,
                                       @PathVariable Long templateId) {
        final Long userId = userDetails.getUserId();
        bossContractFacade.deleteContractTemplate(storeId, userId, templateId);
    }
}