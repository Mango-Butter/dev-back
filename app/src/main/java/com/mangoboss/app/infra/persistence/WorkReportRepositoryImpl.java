package com.mangoboss.app.infra.persistence;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.WorkReportRepository;
import com.mangoboss.storage.workreport.WorkReportEntity;
import com.mangoboss.storage.workreport.WorkReportJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
@RequiredArgsConstructor
public class WorkReportRepositoryImpl implements WorkReportRepository {

    private final WorkReportJpaRepository workReportJpaRepository;

    @Override
    public WorkReportEntity getByStoreIdAndWorkReportId(Long storeId, Long workReportId) {
        return workReportJpaRepository.findByStoreIdAndId(storeId, workReportId)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.WORK_REPORT_NOT_FOUND));
    }

    @Override
    public List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(final Long storeId, final LocalDate date) {
        return workReportJpaRepository.findByStoreIdAndDateOrderByCreatedAtDesc(storeId, date);
    }
}