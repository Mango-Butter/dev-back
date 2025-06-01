package com.mangoboss.storage.schedule;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.attendance.AttendanceEntity;
import com.mangoboss.storage.staff.StaffEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "schedule")
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
    private ScheduleState state;

    @OneToOne(mappedBy = "schedule", fetch = FetchType.LAZY)
    private AttendanceEntity attendance;

    @Builder
    private ScheduleEntity(final LocalDate workDate, final LocalDateTime startTime, final LocalDateTime endTime,
                           final StaffEntity staff, final RegularGroupEntity regularGroup, final Long storeId, final ScheduleState state) {
        this.workDate = workDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.staff = staff;
        this.regularGroup = regularGroup;
        this.storeId = storeId;
        this.state = state;
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
                .state(ScheduleState.NONE)
                .build();
    }

    public ScheduleEntity update(final LocalDate workDate, final LocalTime startTime, final LocalTime endTime) {
        this.workDate = workDate;
        this.startTime = LocalDateTime.of(workDate, startTime);
        this.endTime = adjustEndDateTime(workDate, startTime, endTime);
        this.regularGroup = null;
        return this;
    }

    private static LocalDateTime adjustEndDateTime(final LocalDate workDate, final LocalTime startTime, final LocalTime endTime) {
        return endTime.isAfter(startTime) ?
                LocalDateTime.of(workDate, endTime) : LocalDateTime.of(workDate.plusDays(1), endTime);
    }
}
