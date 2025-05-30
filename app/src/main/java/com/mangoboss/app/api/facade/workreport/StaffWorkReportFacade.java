package com.mangoboss.app.api.facade.workreport;

import com.mangoboss.app.common.util.S3PreSignedUrlManager;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.workreport.WorkReportService;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.workreport.request.WorkReportCreateRequest;
import com.mangoboss.app.dto.workreport.response.WorkReportResponse;
import com.mangoboss.storage.metadata.S3FileType;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.workreport.WorkReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StaffWorkReportFacade {

    private final WorkReportService workReportService;
    private final StaffService staffService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    public WorkReportResponse createWorkReport(final Long storeId, final Long userId, final WorkReportCreateRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final WorkReportEntity entity = workReportService.createWorkReport(storeId, staff.getId(), request.content(), request.reportImageUrl(), request.targetType());
        return WorkReportResponse.fromEntity(entity, staff);
    }

    public UploadPreSignedUrlResponse generateWorkReportImageUploadUrl(final Long storeId, final Long userId,
                                                                       final String extension, final String contentType) {
        staffService.getVerifiedStaff(userId, storeId);
        final String key = s3PreSignedUrlManager.generateFileKey(S3FileType.WORK_REPORT, extension);
        return s3PreSignedUrlManager.generateUploadPreSignedUrl(key, contentType);
    }
}