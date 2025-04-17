package com.mangoboss.storage.store;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"store\"")
public class StoreEntity extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "boss_id", nullable = false)
	private UserEntity boss;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	private String businessNumber;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StoreType storeType;

	private LocalDateTime remittanceDate;

	private LocalTime payUnitMinutes;

	private Long remittanceAccount;

	private String inviteCode;

	private String chatLink;

	private LocalTime overtimeLimit;

	@Column(nullable = false)
	private Integer gpsRangeMeters;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private AttendanceMethod attendanceMethod; // ex) GPS, QR, BOTH

	@Column(nullable = false)
	private Double latitude;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private String attendanceQrCode;

	@Builder
	public StoreEntity(
		final Long id,
		final UserEntity boss,
		final String name,
		final String address,
		final String businessNumber,
		final StoreType storeType,
		final LocalDateTime remittanceDate,
		final LocalTime payUnitMinutes,
		final Long remittanceAccount,
		final String inviteCode,
		final String chatLink,
		final LocalTime overtimeLimit,
		final Integer gpsRangeMeters,
		final AttendanceMethod attendanceMethod,
		final Double latitude,
		final Double longitude,
		final String attendanceQrCode
	) {
		this.id = id;
		this.boss = boss;
		this.name = name;
		this.address = address;
		this.businessNumber = businessNumber;
		this.storeType = storeType;
		this.remittanceDate = remittanceDate;
		this.payUnitMinutes = payUnitMinutes;
		this.remittanceAccount = remittanceAccount;
		this.inviteCode = inviteCode;
		this.chatLink = chatLink;
		this.overtimeLimit = overtimeLimit;
		this.gpsRangeMeters = gpsRangeMeters;
		this.attendanceMethod = attendanceMethod;
		this.latitude = latitude;
		this.longitude = longitude;
		this.attendanceQrCode = attendanceQrCode;
	}

	public static StoreEntity create(
		final UserEntity boss,
		final String name,
		final String address,
		final StoreType storeType,
		final String businessNumber,
		final String inviteCode,
		final Double latitude,
		final Double longitude,
		final String attendanceQrCode
	) {
		return StoreEntity.builder()
			.boss(boss)
			.name(name)
			.address(address)
			.storeType(storeType)
			.businessNumber(businessNumber)
			.inviteCode(inviteCode)
			.latitude(latitude)
			.longitude(longitude)
			.gpsRangeMeters(100) // 기본 - 100m
			.attendanceMethod(AttendanceMethod.GPS) //  기본 - GPS
			.attendanceQrCode(attendanceQrCode)
			.build();
	}
}