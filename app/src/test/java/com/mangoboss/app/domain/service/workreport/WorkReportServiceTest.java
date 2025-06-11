package com.mangoboss.app.domain.service.workreport;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.WorkReportRepository;
import com.mangoboss.storage.workreport.WorkReportEntity;
import com.mangoboss.storage.workreport.WorkReportTargetType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class WorkReportServiceTest {

    @InjectMocks
    private WorkReportService workReportService;

    @Mock
    private WorkReportRepository workReportRepository;

    @Test
    void 매장ID와_날짜로_보고서목록을_조회할_수_있다() {
        Long storeId = 1L;
        LocalDate date = LocalDate.now();
        List<WorkReportEntity> mockReports = List.of(mock(WorkReportEntity.class));

        given(workReportRepository.findByStoreIdAndDateOrderByCreatedAtDesc(storeId, date))
                .willReturn(mockReports);

        List<WorkReportEntity> result = workReportService.findByStoreIdAndDateOrderByCreatedAtDesc(storeId, date);

        assertEquals(mockReports, result);
    }

    @Test
    void 알바생용_보고서를_조회할_수_있다() {
        Long storeId = 1L;
        LocalDate date = LocalDate.now();
        List<WorkReportEntity> mockReports = List.of(mock(WorkReportEntity.class));

        given(workReportRepository.findByStoreIdAndDateAndTargetTypeOrderByCreatedAtDesc(storeId, date, WorkReportTargetType.TO_STAFF))
                .willReturn(mockReports);

        List<WorkReportEntity> result = workReportService.findStaffWorkReports(storeId, date);

        assertEquals(mockReports, result);
    }

    @Test
    void 보고서ID와_매장ID로_단일_보고서를_조회할_수_있다() {
        Long storeId = 1L;
        Long reportId = 2L;
        WorkReportEntity report = mock(WorkReportEntity.class);

        given(workReportRepository.getByStoreIdAndWorkReportId(storeId, reportId)).willReturn(report);

        WorkReportEntity result = workReportService.getWorkReportByStoreIdAndId(storeId, reportId);

        assertEquals(report, result);
    }

    @Test
    void 보고서를_생성할_수_있다() {
        Long storeId = 1L;
        Long staffId = 2L;
        String content = "내용";
        String imageUrl = "http://test.com/image.png";
        WorkReportTargetType type = WorkReportTargetType.TO_STAFF;

        WorkReportEntity created = WorkReportEntity.create(storeId, staffId, content, imageUrl, type);
        given(workReportRepository.save(any())).willReturn(created);

        WorkReportEntity result = workReportService.createWorkReport(storeId, staffId, content, imageUrl, type);

        assertEquals(created, result);
    }

    @Test
    void 보스용_보고서이면_접근시_예외를_던진다() {
        Long storeId = 1L;
        Long reportId = 2L;
        WorkReportEntity report = mock(WorkReportEntity.class);

        given(workReportRepository.getByStoreIdAndWorkReportId(storeId, reportId)).willReturn(report);
        given(report.getTargetType()).willReturn(WorkReportTargetType.TO_BOSS);

        CustomException ex = assertThrows(CustomException.class,
                () -> workReportService.validateStaffHasAccessToWorkReport(storeId, reportId)
        );

        assertEquals(CustomErrorInfo.WORK_REPORT_ACCESS_DENIED, ex.getErrorCode());
    }

    @Test
    void 알바생용_보고서이면_정상적으로_접근할_수_있다() {
        Long storeId = 1L;
        Long reportId = 2L;
        WorkReportEntity report = mock(WorkReportEntity.class);

        given(workReportRepository.getByStoreIdAndWorkReportId(storeId, reportId)).willReturn(report);
        given(report.getTargetType()).willReturn(WorkReportTargetType.TO_STAFF);

        assertDoesNotThrow(() -> workReportService.validateStaffHasAccessToWorkReport(storeId, reportId));
    }
}