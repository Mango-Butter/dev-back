package com.mangoboss.storage.workreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WorkReportJpaRepository extends JpaRepository<WorkReportEntity, Long> {

    Optional<WorkReportEntity> findByStoreIdAndId(Long storeId, Long workReportId);

    @Query("""
                SELECT wr FROM WorkReportEntity wr
                WHERE wr.storeId = :storeId
                AND wr.createdAt >= :startDateTime
                AND wr.createdAt < :endDateTime
                ORDER BY wr.createdAt DESC
            """)
    List<WorkReportEntity> findByStoreIdAndDateOrderByCreatedAtDesc(Long storeId, LocalDateTime start, LocalDateTime end);

    @Query("""
                SELECT wr FROM WorkReportEntity wr
                WHERE wr.storeId = :storeId
                AND wr.createdAt >= :startDateTime
                AND wr.createdAt < :endDateTime
                AND wr.targetType = :targetType
                ORDER BY wr.createdAt DESC
            """)
    List<WorkReportEntity> findByStoreIdAndDateAndTargetTypeOrderByCreatedAtDesc(
            Long storeId, LocalDateTime start, LocalDateTime end, WorkReportTargetType targetType
    );
}