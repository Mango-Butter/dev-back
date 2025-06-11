package com.mangoboss.app.domain.service.notification;

import com.mangoboss.app.domain.repository.DeviceTokenRepository;
import com.mangoboss.app.domain.repository.NotificationRepository;
import com.mangoboss.storage.attendance.AttendanceEditEntity;
import com.mangoboss.storage.notification.NotificationEntity;
import com.mangoboss.storage.notification.SendStatus;
import com.mangoboss.storage.schedule.SubstituteRequestEntity;
import com.mangoboss.storage.notification.NotificationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private DeviceTokenRepository deviceTokenRepository;

    @BeforeEach
    void setup() {
        notificationService = new NotificationService(notificationRepository, deviceTokenRepository);
        // frontendUrl 직접 세팅
        notificationService.getClass().getDeclaredFields();
        // reflection 방식으로 @Value 설정 우회
        try {
            var field = NotificationService.class.getDeclaredField("frontendUrl");
            field.setAccessible(true);
            field.set(notificationService, "https://frontend.com");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void 근로계약서알림_토큰없으면_토큰없이저장() {
        when(deviceTokenRepository.findActiveTokensByUserId(1L)).thenReturn(List.of());

        notificationService.saveContractSignNotification(1L, 10L);

        verify(notificationRepository).save(argThat(notif ->
                notif.getUserId().equals(1L) &&
                        notif.getTargetToken() == null &&  // ✅ 수정된 부분
                        notif.getType() == NotificationType.CONTRACT &&
                        notif.getSendStatus() == SendStatus.PENDING &&
                        notif.getRetryCount() == 0
        ));
    }

    @Test
    void 근로계약서알림_토큰있으면_모든토큰에저장() {
        when(deviceTokenRepository.findActiveTokensByUserId(1L)).thenReturn(List.of("t1", "t2"));

        notificationService.saveContractSignNotification(1L, 10L);

        verify(notificationRepository, times(2)).save(any(NotificationEntity.class));
    }

    @Test
    void 대타승인알림_양쪽저장() {
        SubstituteRequestEntity request = mock(SubstituteRequestEntity.class);
        when(request.getRequesterStaffId()).thenReturn(1L);
        when(request.getTargetStaffId()).thenReturn(2L);
        when(request.getStoreId()).thenReturn(10L);
        when(request.getRequesterStaffName()).thenReturn("홍길동");
        when(request.getTargetStaffName()).thenReturn("김영희");

        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of());

        notificationService.saveSubstituteApproveNotificationForBoth(request);

        verify(notificationRepository, times(2)).save(any(NotificationEntity.class));
    }

    @Test
    void 근태요청알림_사장에게보내기() {
        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of("t1"));

        notificationService.saveAttendanceEditRequestNotification(100L, 5L, "김철수");

        verify(notificationRepository).save(argThat(notif ->
                notif.getTitle().contains("근태기록") &&
                        notif.getUserId().equals(100L)
        ));
    }

    @Test
    void 보고사항알림_사장에게보내기() {
        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of());

        notificationService.saveWorkReportNotificationToBoss(200L, 1L, "이몽룡");

        verify(notificationRepository).save(argThat(notif ->
                notif.getType() == NotificationType.WORK_REPORT &&
                        notif.getUserId().equals(200L)
        ));
    }

    @Test
    void 대타요청알림_정상저장된다() {
        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of());

        notificationService.saveSubstituteRequestNotification(1L, 10L, "김철수");

        verify(notificationRepository).save(argThat(notif ->
                notif.getUserId().equals(1L) &&
                        notif.getStoreId().equals(10L) &&
                        notif.getTitle().contains("대타 요청") &&
                        notif.getContent().contains("김철수")
        ));
    }

    @Test
    void 대타거절알림_요청자_대상자_모두저장된다() {
        SubstituteRequestEntity request = mock(SubstituteRequestEntity.class);
        when(request.getRequesterStaffId()).thenReturn(1L);
        when(request.getTargetStaffId()).thenReturn(2L);
        when(request.getStoreId()).thenReturn(10L);
        when(request.getRequesterStaffName()).thenReturn("홍길동");
        when(request.getTargetStaffName()).thenReturn("김영희");

        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of());

        notificationService.saveSubstituteRejectNotificationForBoth(request);

        verify(notificationRepository, times(2)).save(argThat(notif ->
                notif.getUserId().equals(1L) || notif.getUserId().equals(2L)
        ));
    }

    @Test
    void 근태승인알림_정상저장된다() {
        AttendanceEditEntity edit = mock(AttendanceEditEntity.class);
        when(edit.getStaffId()).thenReturn(1L);
        when(edit.getStoreId()).thenReturn(10L);

        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of());

        notificationService.saveAttendanceEditApproveNotification(edit);

        verify(notificationRepository).save(argThat(notif ->
                notif.getUserId().equals(1L) &&
                        notif.getTitle().contains("승인") &&
                        notif.getType() == NotificationType.SUBSTITUTE
        ));
    }

    @Test
    void 근태거절알림_정상저장된다() {
        AttendanceEditEntity edit = mock(AttendanceEditEntity.class);
        when(edit.getStaffId()).thenReturn(1L);
        when(edit.getStoreId()).thenReturn(10L);

        when(deviceTokenRepository.findActiveTokensByUserId(anyLong())).thenReturn(List.of("t1"));

        notificationService.saveAttendanceEditRejectNotification(edit);

        verify(notificationRepository).save(argThat(notif ->
                notif.getUserId().equals(1L) &&
                        notif.getTitle().contains("거절") &&
                        notif.getType() == NotificationType.SUBSTITUTE
        ));
    }

    @Test
    void 유저와매장으로_알림목록을_조회할_수_있다() {
        when(notificationRepository.findByUserIdAndStoreIdOrderByCreatedAtDesc(1L, 10L))
                .thenReturn(List.of(mock(NotificationEntity.class)));

        List<NotificationEntity> result = notificationService.getNotificationsByUserAndStore(1L, 10L);

        verify(notificationRepository).findByUserIdAndStoreIdOrderByCreatedAtDesc(1L, 10L);
        assertThat(result).hasSize(1);
    }
}
