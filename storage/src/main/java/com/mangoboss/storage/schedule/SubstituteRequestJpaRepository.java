package com.mangoboss.storage.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubstituteRequestJpaRepository extends JpaRepository<SubstituteRequestEntity, Long> {

    List<SubstituteRequestEntity> findAllByRequesterStaffIdOrTargetStaffId(Long requesterStaffId, Long targetStaffId);

    Boolean existsByRequestScheduleId(Long requestScheduleId);
}
