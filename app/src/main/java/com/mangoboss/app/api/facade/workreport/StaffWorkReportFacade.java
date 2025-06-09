package com.mangoboss.app.api.facade.workreport;

import com.mangoboss.app.common.util.S3PreSignedUrlManager;
import com.mangoboss.app.domain.service.notification.NotificationService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.workreport.WorkReportService;
import com.mangoboss.app.dto.s3.response.UploadPreSignedUrlResponse;
import com.mangoboss.app.dto.workreport.request.WorkReportCreateRequest;
import com.mangoboss.app.dto.workreport.response.WorkReportResponse;
import com.mangoboss.storage.metadata.S3FileType;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.workreport.WorkReportEntity;
import com.mangoboss.storage.workreport.WorkReportTargetType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffWorkReportFacade {

    private final WorkReportService workReportService;
    private final StaffService staffService;
    private final NotificationService notificationService;
    private final S3PreSignedUrlManager s3PreSignedUrlManager;

    public WorkReportResponse createWorkReport(final Long storeId, final Long userId, final WorkReportCreateRequest request) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        final WorkReportEntity entity = workReportService.createWorkReport(storeId, staff.getId(), request.content(), request.reportImageUrl(), request.targetType());

        if (request.targetType() == WorkReportTargetType.TO_BOSS) {
            final Long bossUserId = staff.getStore().getBoss().getId();
            notificationService.saveWorkReportNotificationToBoss(bossUserId, storeId, staff.getUser().getName());
        }

        return WorkReportResponse.fromEntity(entity, staff);
    }

    public List<WorkReportResponse> getWorkReportsByDate(final Long storeId, final Long userId, final LocalDate date) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        return workReportService.findStaffWorkReports(storeId, date).stream()
                .map(workReport -> WorkReportResponse.fromEntity(workReport, staff))
                .toList();
    }

    public WorkReportResponse getWorkReportDetail(final Long storeId, final Long userId, final Long workReportId) {
        StaffEntity staff = staffService.getVerifiedStaff(userId, storeId);
        workReportService.validateStaffHasAccessToWorkReport(storeId, workReportId);
        final WorkReportEntity workReport = workReportService.getWorkReportByStoreIdAndId(storeId, workReportId);
        return WorkReportResponse.fromEntity(workReport, staff);
    }

    public UploadPreSignedUrlResponse generateWorkReportImageUploadUrl(final Long storeId, final Long userId,
                                                                       final String extension, final String contentType) {
        staffService.getVerifiedStaff(userId, storeId);
        final String key = s3PreSignedUrlManager.generateFileKey(S3FileType.WORK_REPORT, extension);
        return s3PreSignedUrlManager.generateUploadPreSignedUrl(key, contentType);
    }
}