package com.mangoboss.app.domain.service.workreport;

import com.mangoboss.app.domain.repository.WorkReportRepository;
import com.mangoboss.storage.workreport.WorkReportEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkReportService {

    private final WorkReportRepository workReportRepository;

    public List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(final Long storeId, final LocalDate date) {
        return workReportRepository.findByStoreIdAndDateOrderByCreatedAtDesc(storeId, date);
    }

    public WorkReportEntity getWorkReportByStoreIdAndId(final Long storeId, final Long workReportId) {
        return workReportRepository.getByStoreIdAndWorkReportId(storeId, workReportId);
    }
}