package com.mangoboss.app.api.controller.workreport;

import com.mangoboss.app.api.facade.workreport.StaffWorkReportFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.ListWrapperResponse;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.workreport.request.WorkReportCreateRequest;
import com.mangoboss.app.dto.workreport.response.WorkReportResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/staff/stores/{storeId}/work-reports")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STAFF')")
public class StaffWorkReportController {

    private final StaffWorkReportFacade staffWorkReportFacade;

    @PostMapping
    public WorkReportResponse createWorkReport(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @PathVariable Long storeId,
                                               @RequestBody @Valid WorkReportCreateRequest request) {
        final Long userId = userDetails.getUserId();
        return staffWorkReportFacade.createWorkReport(storeId, userId, request);
    }

    @GetMapping
    public ListWrapperResponse<WorkReportResponse> getWorkReportsByDate(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                        @PathVariable Long storeId,
                                                                        @RequestParam LocalDate date) {
        final Long userId = userDetails.getUserId();
        return ListWrapperResponse.of(staffWorkReportFacade.getWorkReportsByDate(storeId, userId, date));
    }

    @GetMapping("/{workReportId}")
    public WorkReportResponse getWorkReportDetail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable Long storeId,
                                                  @PathVariable Long workReportId) {
        final Long userId = userDetails.getUserId();
        return staffWorkReportFacade.getWorkReportDetail(storeId, userId, workReportId);
    }

    @GetMapping("/work-report-image/upload-url")
    public UploadPreSignedUrlResponse generateWorkReportImageUploadUrl(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @PathVariable Long storeId,
                                                                       @RequestParam final String extension,
                                                                       @RequestParam final String contentType) {
        final Long userId = userDetails.getUserId();
        return staffWorkReportFacade.generateWorkReportImageUploadUrl(storeId, userId, extension, contentType);
    }
}