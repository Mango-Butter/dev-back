package com.mangoboss.storage.attendance;

import java.time.LocalDateTime;

import com.mangoboss.storage.BaseTimeEntity;

import com.mangoboss.storage.schedule.ScheduleEntity;
import jakarta.persistence.*;
import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "attendance")
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

    @Builder
    public AttendanceEntity(final LocalDateTime clockInTime, final LocalDateTime clockOutTime,
                            final ClockInStatus clockInStatus, final ClockOutStatus clockOutStatus, final ScheduleEntity schedule) {
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.clockInStatus = clockInStatus;
        this.clockOutStatus = clockOutStatus;
        this.schedule = schedule;
    }

    public static AttendanceEntity create(final LocalDateTime clockInTime, final LocalDateTime clockOutTime,
                                          final ClockInStatus clockInStatus, final ClockOutStatus clockOutStatus, final ScheduleEntity schedule) {
        return AttendanceEntity.builder()
                .clockInTime(clockInTime)
                .clockOutTime(clockOutTime)
                .clockInStatus(clockInStatus)
                .clockOutStatus(clockOutStatus)
                .schedule(schedule)
                .build();
    }

    public static AttendanceEntity createForClockIn(final ScheduleEntity schedule, final LocalDateTime clockInTime, ClockInStatus clockInStatus) {
        return AttendanceEntity.builder()
                .schedule(schedule)
                .clockInTime(clockInTime)
                .clockInStatus(clockInStatus)
                .build();
    }

    public boolean isAlreadyClockedOut() {
        return this.clockOutStatus != null;
    }

    public void recordClockOut(LocalDateTime time, ClockOutStatus clockOutStatus) {
        this.clockOutTime = time;
        this.clockOutStatus = clockOutStatus;
    }

    public AttendanceEntity update(final LocalDateTime clockInTime, final LocalDateTime clockOutTime,
                                   final ClockInStatus clockInStatus, final ClockOutStatus clockOutStatus) {
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.clockInStatus = clockInStatus;
        this.clockOutStatus = clockOutStatus;
        return this;
    }
}