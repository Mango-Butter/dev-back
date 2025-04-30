package com.mangoboss.storage.attendance;

import java.time.Clock;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "schedule_id", nullable = false)
	private ScheduleEntity schedule;

	@Column(nullable = false)
	private LocalDateTime clockInTime;

	private LocalDateTime clockOutTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ClockInStatus clockInStatus;

	@Enumerated(EnumType.STRING)
	private ClockOutStatus clockOutStatus;

	@Builder
	public AttendanceEntity(final Long id, final LocalDateTime clockInTime,
							final ScheduleEntity schedule, final ClockInStatus clockInStatus) {
		this.id = id;
		this.clockInTime = clockInTime;
		this.schedule = schedule;
		this.clockInStatus = clockInStatus;
	}

	public static AttendanceEntity create(final ScheduleEntity schedule, final LocalDateTime clockInTime, ClockInStatus clockInStatus) {
		return AttendanceEntity.builder()
				.schedule(schedule)
				.clockInTime(clockInTime)
				.clockInStatus(clockInStatus)
				.build();
	}

	public boolean isAlreadyClockedOut() {
		return this.clockOutTime != null;
	}

	public void recordClockOut(LocalDateTime time, ClockOutStatus clockOutStatus) {
		this.clockOutTime = time;
		this.clockOutStatus = clockOutStatus;
	}
}