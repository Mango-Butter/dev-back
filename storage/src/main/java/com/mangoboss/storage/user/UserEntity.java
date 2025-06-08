package com.mangoboss.storage.user;

import com.mangoboss.storage.BaseTimeEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "\"user\"")
public class UserEntity extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String name;

	private String password;

	@Column(nullable = false)
	private String phone;

	@Column(unique = true, nullable = false)
	private Long kakaoId;

	@Column(nullable = false)
	private LocalDate birth;

	@Column(nullable = false)
	private String profileImageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Builder
	private UserEntity(final Long kakaoId, final String name, final String email, final String phone,
		final LocalDate birth, final String profileImageUrl, final Role role, final LocalDateTime createdAt
	) {
		this.kakaoId = kakaoId;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.birth = birth;
		this.profileImageUrl = profileImageUrl;
		this.role = role;
	}

	public static UserEntity create(final Long kakaoId, final String name, final String email, final String phone,
				final LocalDate birth, final String profileImageUrl, final Role role) {
		return UserEntity.builder()
			.kakaoId(kakaoId)
			.name(name)
			.email(email)
			.phone(phone)
			.birth(birth)
			.profileImageUrl(profileImageUrl)
			.role(role)
			.build();
	}

	public void assignRole(final Role newRole) {
		this.role = newRole;
	}
}