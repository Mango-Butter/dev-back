package com.mangoboss.storage.attendance;

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
@Table(name = "attendance_edit")
public class AttendanceEditEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attendance_id", nullable = false)
    private Long attendanceId;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private Long staffId;

    @Column(nullable = false)
    private String staffName;

    private LocalDateTime requestedClockInTime;

    private LocalDateTime requestedClockOutTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClockInStatus requestedClockInStatus;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceEditState attendanceEditState;

    @Column(nullable = false)
    private LocalDate originalWorkDate;

    private LocalDateTime originalClockInTime;

    private LocalDateTime originalClockOutTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClockInStatus originalClockInStatus;

    @Builder
    private AttendanceEditEntity(
            final Long attendanceId,
            final Long storeId,
            final Long staffId,
            final String staffName,
            final LocalDateTime requestedClockInTime,
            final LocalDateTime requestedClockOutTime,
            final ClockInStatus requestedClockInStatus,
            final String reason,
            final AttendanceEditState attendanceEditState,
            final LocalDate originalWorkDate,
            final LocalDateTime originalClockInTime,
            final LocalDateTime originalClockOutTime,
            final ClockInStatus originalClockInStatus) {
        this.attendanceId = attendanceId;
        this.storeId = storeId;
        this.staffId = staffId;
        this.staffName = staffName;
        this.requestedClockInTime = requestedClockInTime;
        this.requestedClockOutTime = requestedClockOutTime;
        this.requestedClockInStatus = requestedClockInStatus;
        this.reason = reason;
        this.attendanceEditState = attendanceEditState;
        this.originalWorkDate = originalWorkDate;
        this.originalClockInTime = originalClockInTime;
        this.originalClockOutTime = originalClockOutTime;
        this.originalClockInStatus = originalClockInStatus;
    }

    public static AttendanceEditEntity create(
            final AttendanceEntity original,
            final LocalDateTime requestedClockInTime,
            final LocalDateTime requestedClockOutTime,
            final ClockInStatus requestedClockInStatus,
            final String reason) {
        StaffEntity staff = original.getSchedule().getStaff();
        return AttendanceEditEntity.builder()
                .attendanceId(original.getId())
                .storeId(staff.getStore().getId())
                .staffId(staff.getId())
                .staffName(staff.getName())
                .requestedClockInTime(requestedClockInTime)
                .requestedClockOutTime(requestedClockOutTime)
                .requestedClockInStatus(requestedClockInStatus)
                .reason(reason)
                .attendanceEditState(AttendanceEditState.PENDING)
                .originalWorkDate(original.getSchedule().getWorkDate())
                .originalClockInTime(original.getClockInTime())
                .originalClockOutTime(original.getClockOutTime())
                .originalClockInStatus(original.getClockInStatus())
                .build();
    }

    public AttendanceEditEntity approved() {
        this.attendanceEditState = AttendanceEditState.APPROVED;
        return this;
    }

    public AttendanceEditEntity rejected() {
        this.attendanceEditState = AttendanceEditState.REJECTED;
        return this;
    }
}
