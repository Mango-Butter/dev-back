package com.mangoboss.storage;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long id;

	@Column(unique = true)
	private String email;

	@Column
	private String name;

	@Column
	private String password;

	@Column
	private String phone;

	@Column(unique = true, nullable = false)
	private Long kakaoId;

	@Column
	private LocalDate birthday;

	@Column
	private String profileImage;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Builder
	public Member(Long id, String email, String name, String phone, Long kakaoId, LocalDate birthday, String profileImage, Role role) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.phone = phone;
		this.kakaoId = kakaoId;
		this.birthday = birthday;
		this.profileImage = profileImage;
		this.role = role;
	}
}