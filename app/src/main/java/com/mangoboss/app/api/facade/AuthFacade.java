package com.mangoboss.app.api.facade;

import java.time.LocalDateTime;
import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.auth.OAuth;
import com.mangoboss.app.auth.UserInfoGetDto;
import com.mangoboss.app.domain.UserService;
import com.mangoboss.app.dto.LoginRequest;
import com.mangoboss.app.dto.LoginResponse;
import com.mangoboss.app.dto.ReissueTokenDto;
import com.mangoboss.app.exception.CustomErrorCode;
import com.mangoboss.app.exception.CustomException;
import com.mangoboss.app.util.JwtUtil;
import com.mangoboss.storage.Role;
import com.mangoboss.storage.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthFacade {
	private final JwtUtil jwtUtil;
	private final OAuth oAuth;
	private final UserService userService;

	@Transactional
	public LoginResponse reissueAccessToken(ReissueTokenDto refreshToken) {
		String token = refreshToken.getRefreshToken();
		if (!jwtUtil.validateToken(token)) {
			throw new CustomException(CustomErrorCode.UNAUTHORIZED);
		}

		Long kakaoId = jwtUtil.getKakaoId(token);
		User user = userService.findByKakaoIdOrThrow(kakaoId);

		String accessToken = jwtUtil.createAccessToken(user.getKakaoId(), user.getRole().toString());

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(token)
			.build();
	}

	public LoginResponse socialLogin(LoginRequest loginRequest) throws LoginException {
		boolean isNewUser = false;

		String kakaoAccessToken = oAuth.requestKakaoAccessToken(loginRequest);

		UserInfoGetDto userInfoGetDto = oAuth.getUserInfoFromKakao(kakaoAccessToken);

		Optional<User> userOptional = userService.findByKakaoId(userInfoGetDto.getKakaoId());

		User user;

		if (userOptional.isPresent()) {
			user = userOptional.get();

			// 기존 회원이지만, 회원가입 미완료 상태 (UNREGISTER)면 새 회원으로 판단
			if (Role.UNASSIGNED.equals(user.getRole())) {
				isNewUser = true;
			}
		} else {
			user = User.builder()
				.email(userInfoGetDto.getEmail())
				.name(userInfoGetDto.getName())
				.phone(userInfoGetDto.getPhone())
				.kakaoId(userInfoGetDto.getKakaoId())
				.birth(userInfoGetDto.getBirth())
				.profileImageUrl(userInfoGetDto.getPicture())
				.createdAt(LocalDateTime.now())
				.role(Role.UNASSIGNED)
				.build();

			userService.save(user);
			isNewUser = true;
		}

		String accessToken = jwtUtil.createAccessToken(user.getKakaoId(), user.getRole().toString());
		String refreshToken = jwtUtil.createRefreshToken(user.getKakaoId(), user.getRole().toString());

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isNewUser(isNewUser)
			.build();
	}
}
