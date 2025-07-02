package com.mangoboss.storage.attendance;

import java.time.LocalDateTime;

import com.mangoboss.storage.BaseTimeEntity;

import com.mangoboss.storage.schedule.ScheduleEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
        name = "attendance",
        indexes = {
                @Index(name = "idx_attendance_schedule_id", columnList = "schedule_id"),
        }
)
public class AttendanceEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private ScheduleEntity schedule;

    private LocalDateTime clockInTime;

    private LocalDateTime clockOutTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClockInStatus clockInStatus;

    @Enumerated(EnumType.STRING)
    private ClockOutStatus clockOutStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceState attendanceState;

    @Builder
    private AttendanceEntity(final LocalDateTime clockInTime, final LocalDateTime clockOutTime,
                             final ClockInStatus clockInStatus, final ClockOutStatus clockOutStatus, final ScheduleEntity schedule,
                             final AttendanceState attendanceState) {
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.clockInStatus = clockInStatus;
        this.clockOutStatus = clockOutStatus;
        this.schedule = schedule;
        this.attendanceState = attendanceState;
    }

    public static AttendanceEntity create(final LocalDateTime clockInTime, final LocalDateTime clockOutTime,
                                          final ClockInStatus clockInStatus, final ClockOutStatus clockOutStatus, final ScheduleEntity schedule) {
        return AttendanceEntity.builder()
                .clockInTime(clockInTime)
                .clockOutTime(clockOutTime)
                .clockInStatus(clockInStatus)
                .clockOutStatus(clockOutStatus)
                .schedule(schedule)
                .attendanceState(AttendanceState.NONE)
                .build();
    }

    public static AttendanceEntity createForClockIn(final ScheduleEntity schedule, final LocalDateTime clockInTime, ClockInStatus clockInStatus) {
        return AttendanceEntity.builder()
                .schedule(schedule)
                .clockInTime(clockInTime)
                .clockInStatus(clockInStatus)
                .attendanceState(AttendanceState.NONE)
                .build();
    }

    public boolean isAlreadyClockedOut() {
        return this.clockOutStatus != null;
    }

    public AttendanceEntity recordClockOut(LocalDateTime time, ClockOutStatus clockOutStatus) {
        this.clockOutTime = time;
        this.clockOutStatus = clockOutStatus;
        return this;
    }

    public AttendanceEntity update(final LocalDateTime clockInTime, final LocalDateTime clockOutTime,
                                   final ClockInStatus clockInStatus, final ClockOutStatus clockOutStatus) {
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.clockInStatus = clockInStatus;
        this.clockOutStatus = clockOutStatus;
        return this;
    }

    public Integer calculateWorkTime(final Integer deductionUnit) {
        if (clockInTime == null || clockOutTime == null) {
            return 0;
        }
        long totalMinutes = java.time.Duration.between(clockInTime, clockOutTime).toMinutes();
        return (int) (totalMinutes / deductionUnit) * deductionUnit;
    }

    public boolean isCompleted() {
        return this.clockInStatus != null
                && this.clockOutStatus != null;
    }

    public boolean isRequested() {
        return this.attendanceState.equals(AttendanceState.REQUESTED);
    }

    public void requested() {
        this.attendanceState = AttendanceState.REQUESTED;
    }

    public void edited() {
        this.attendanceState = AttendanceState.EDITED;
    }

    public void none() {
        this.attendanceState = AttendanceState.NONE;
    }
}