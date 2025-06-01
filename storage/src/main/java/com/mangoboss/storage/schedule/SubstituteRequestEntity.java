package com.mangoboss.storage.schedule;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.staff.StaffEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "substitute_request")
public class SubstituteRequestEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubstituteState state;

    @Column(nullable = false)
    private String reason;

    @Column(name = "requester_staff_id", nullable = false)
    private Long requesterStaffId;

    @Column(name = "reqeuste_schedule_id", nullable = false)
    private Long requestScheduleId;

    @Column(name = "target_staff_id", nullable = false)
    private Long targetStaffId;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String requesterStaffName;

    @Column(nullable = false)
    private String targetStaffName;

    @Builder
    private SubstituteRequestEntity(final SubstituteState state, final String reason, final Long requesterStaffId,
                                    final Long requestScheduleId, final Long targetStaffId,
                                    final LocalDate workDate, final LocalDateTime startTime, final LocalDateTime endTime,
                                    final String requesterName, final String targetName) {
        this.state = state;
        this.reason = reason;
        this.requesterStaffId = requesterStaffId;
        this.requestScheduleId = requestScheduleId;
        this.targetStaffId = targetStaffId;
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.requesterStaffName = requesterName;
        this.targetStaffName = targetName;
    }

    public static SubstituteRequestEntity create(
            final String reason,
            final StaffEntity requesterStaff,
            final StaffEntity targetStaff,
            final ScheduleEntity schedule) {
        return SubstituteRequestEntity.builder()
                .state(SubstituteState.PENDING)
                .reason(reason)
                .requesterStaffId(requesterStaff.getId())
                .requesterName(requesterStaff.getName())
                .requestScheduleId(schedule.getId())
                .targetStaffId(targetStaff.getId())
                .targetName(targetStaff.getName())
                .workDate(schedule.getWorkDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .build();
    }
}
