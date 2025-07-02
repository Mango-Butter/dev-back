package com.mangoboss.storage.schedule;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.staff.StaffEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "schedule",
        indexes = {
                @Index(name = "idx_schedule_work_date", columnList = "store_id, work_date"),
                @Index(name = "idx_end_time", columnList = "end_time"),
                @Index(name = "idx_schedule_staff_workdate", columnList = "staff_id, work_date")
        }
)
public class ScheduleEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id")
    private StaffEntity staff;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "regular_group_id")
    private RegularGroupEntity regularGroup;

    @Column(nullable = false)
    private LocalDate workDate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubstitutionState substitutionState;

    @OneToOne(mappedBy = "schedule", fetch = FetchType.LAZY)
    private AttendanceEntity attendance;

    @Builder
    private ScheduleEntity(final LocalDate workDate, final LocalDateTime startTime, final LocalDateTime endTime,
                           final StaffEntity staff, final RegularGroupEntity regularGroup, final Long storeId, final SubstitutionState substitutionState) {
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.staff = staff;
        this.regularGroup = regularGroup;
        this.storeId = storeId;
        this.substitutionState = substitutionState;
    }

    public static ScheduleEntity create(final LocalDate workDate, final LocalTime startTime, final LocalTime endTime,
                                        final StaffEntity staff, final RegularGroupEntity regularGroup, final Long storeId) {
        return ScheduleEntity.builder()
                .workDate(workDate)
                .startTime(LocalDateTime.of(workDate, startTime))
                .endTime(adjustEndDateTime(workDate, startTime, endTime))
                .staff(staff)
                .regularGroup(regularGroup)
                .storeId(storeId)
                .substitutionState(SubstitutionState.NONE)
                .build();
    }

    public ScheduleEntity update(final LocalDate workDate, final LocalTime startTime, final LocalTime endTime) {
        this.workDate = workDate;
        this.startTime = LocalDateTime.of(workDate, startTime);
        this.endTime = adjustEndDateTime(workDate, startTime, endTime);
        this.regularGroup = null;
        this.substitutionState = SubstitutionState.NONE;
        return this;
    }

    private static LocalDateTime adjustEndDateTime(final LocalDate workDate, final LocalTime startTime, final LocalTime endTime) {
        return endTime.isAfter(startTime) ?
                LocalDateTime.of(workDate, endTime) : LocalDateTime.of(workDate.plusDays(1), endTime);
    }

    public Boolean isRequested() {
        return substitutionState.equals(SubstitutionState.REQUESTED);
    }

    public void requested() {
        this.substitutionState = SubstitutionState.REQUESTED;
    }

    public void substituted(final StaffEntity staff) {
        this.staff = staff;
        this.substitutionState = SubstitutionState.SUBSTITUTED;
    }

    public void rejected() {
        this.substitutionState = SubstitutionState.NONE;
    }
}
