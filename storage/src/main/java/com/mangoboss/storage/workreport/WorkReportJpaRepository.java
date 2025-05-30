package com.mangoboss.storage.workreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkReportJpaRepository extends JpaRepository<WorkReportEntity, Long> {

    Optional<WorkReportEntity> findByStoreIdAndId(Long storeId, Long workReportId);

    @Query("""
                SELECT wr FROM WorkReportEntity wr
                WHERE wr.storeId = :storeId
                AND DATE(wr.createdAt) = :date
                ORDER BY wr.createdAt DESC
            """)
    List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(Long storeId, LocalDate date);
}