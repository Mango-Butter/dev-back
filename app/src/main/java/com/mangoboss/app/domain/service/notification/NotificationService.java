package com.mangoboss.app.domain.service.notification;

import com.mangoboss.app.domain.repository.DeviceTokenRepository;
import com.mangoboss.app.domain.repository.NotificationRepository;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.NotificationType;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    @Value("${frontend-url}")
    private String frontendUrl;

    private final NotificationRepository notificationRepository;
    private final DeviceTokenRepository deviceTokenRepository;

    private void saveNotification(final Long userId, final Long storeId, final String title,
                                  final String content, final NotificationType type, final String path) {
        final String clickUrl = frontendUrl + path;
        final List<String> tokens = deviceTokenRepository.findActiveTokensByUserId(userId);

        if (tokens.isEmpty()) {
            final NotificationEntity notification = NotificationEntity.create(userId, storeId, title, content, null, clickUrl, type, null);
            notificationRepository.save(notification);
        } else {
            for (String token : tokens) {
                final NotificationEntity notification = NotificationEntity.create(userId, storeId, title, content, null, clickUrl, type, token);
                notificationRepository.save(notification);
            }
        }
    }

    @Transactional
    public void saveContractSignNotification(final Long userId, final Long storeId) {
        saveNotification(
                userId,
                storeId,
                "근로계약서 서명 요청",
                "사장님이 근로계약서 서명 요청을 보냈어요.",
                NotificationType.CONTRACT,
                "/staff/document?type=contract"
        );
    }

    @Transactional
    public void saveSubstituteRequestNotification(final Long userId, final Long storeId, final String staffName) {
        String content = String.format("%s님이 대타요청을 보냈어요.", staffName);
        saveNotification(
                userId,
                storeId,
                "대타 요청",
                content,
                NotificationType.SUBSTITUTE,
                "/boss/alarm?type=request"
        );
    }

    private void saveSubstituteApproveNotification(final Long userId, final Long storeId, final String requesterName, final String targetName) {
        String content = String.format("%s님과 %s님의 대타요청이 승인되었어요.", requesterName, targetName);
        saveNotification(
                userId,
                storeId,
                "대타 요청 승인",
                content,
                NotificationType.SUBSTITUTE,
                "/staff/alarm?type=request"
        );
    }

    private void saveSubstituteRejectNotification(final Long userId, final Long storeId, final String requesterName, final String targetName) {
        String content = String.format("%s님과 %s님의 대타요청이 거절되었어요.", requesterName, targetName);
        saveNotification(
                userId,
                storeId,
                "대타 요청 거절",
                content,
                NotificationType.SUBSTITUTE,
                "/staff/alarm?type=request"
        );
    }

    @Transactional
    public void saveSubstituteApproveNotificationForBoth(final SubstituteRequestEntity substituteRequest) {
        saveSubstituteApproveNotification(
                substituteRequest.getRequesterStaffId(),
                substituteRequest.getStoreId(),
                substituteRequest.getRequesterStaffName(),
                substituteRequest.getTargetStaffName()
        );
        saveSubstituteApproveNotification(
                substituteRequest.getTargetStaffId(),
                substituteRequest.getStoreId(),
                substituteRequest.getRequesterStaffName(),
                substituteRequest.getTargetStaffName()
        );
    }

    @Transactional
    public void saveSubstituteRejectNotificationForBoth(final SubstituteRequestEntity substituteRequest) {
        saveSubstituteRejectNotification(
                substituteRequest.getRequesterStaffId(),
                substituteRequest.getStoreId(),
                substituteRequest.getRequesterStaffName(),
                substituteRequest.getTargetStaffName()
        );
        saveSubstituteRejectNotification(
                substituteRequest.getTargetStaffId(),
                substituteRequest.getStoreId(),
                substituteRequest.getRequesterStaffName(),
                substituteRequest.getTargetStaffName()
        );
    }

    @Transactional
    public void saveAttendanceEditRequestNotification(final Long userId, final Long storeId, final String staffName) {
        String content = String.format("%s님이 근태기록 변경을 요청했어요",staffName);
        saveNotification(
                userId,
                storeId,
                "근태기록 변경 요청",
                content,
                NotificationType.SUBSTITUTE,
                "/boss/alarm?type=request"
        );
    }

    @Transactional
    public void saveAttendanceEditApproveNotification(final AttendanceEditEntity attendanceEdit) {
        String content = "사장님이 근태기록 변경을 승인했어요.";
        saveNotification(
                attendanceEdit.getStaffId(),
                attendanceEdit.getStoreId(),
                "근태기록 변경 승인",
                content,
                NotificationType.SUBSTITUTE,
                "/staff/alarm?type=request"
        );
    }

    @Transactional
    public void saveAttendanceEditRejectNotification(final AttendanceEditEntity attendanceEdit) {
        String content = "사장님이 근태기록 변경을 거절했어요.";
        saveNotification(
                attendanceEdit.getStaffId(),
                attendanceEdit.getStoreId(),
                "근태기록 변경 거절",
                content,
                NotificationType.SUBSTITUTE,
                "/staff/alarm?type=request"
        );
    }

    public List<NotificationEntity> getNotificationsByUserAndStore(final Long userId, final Long storeId) {
        return notificationRepository.findByUserIdAndStoreIdOrderByCreatedAtDesc(userId, storeId);
    }

    @Transactional
    public void saveWorkReportNotificationToBoss(final Long bossUserId, final Long storeId, final String staffName) {
        String content = String.format("%s님이 보고사항을 작성했어요.", staffName);
        saveNotification(
                bossUserId,
                storeId,
                "보고사항 작성",
                content,
                NotificationType.WORK_REPORT,
                "/boss/task?type=report"
        );
    }
}
