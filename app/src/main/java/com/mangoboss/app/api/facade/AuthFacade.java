package com.mangoboss.app.api.facade;

import java.util.Optional;

import javax.security.auth.login.LoginException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.auth.OAuth;
import com.mangoboss.app.auth.UserInfoGetDto;
import com.mangoboss.app.domain.MemberService;
import com.mangoboss.app.dto.LoginRequest;
import com.mangoboss.app.dto.LoginResponse;
import com.mangoboss.app.dto.MemberDto;
import com.mangoboss.app.dto.ReissueTokenDto;
import com.mangoboss.app.exception.CustomErrorCode;
import com.mangoboss.app.exception.CustomException;
import com.mangoboss.app.util.JwtUtil;
import com.mangoboss.storage.Member;
import com.mangoboss.storage.Role;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthFacade {
	private final JwtUtil jwtUtil;
	private final OAuth oAuth;
	private final MemberService memberService;

	@Transactional
	public LoginResponse reissueAccessToken(ReissueTokenDto refreshToken) {
		String token = refreshToken.getRefreshToken();
		if (!jwtUtil.validateToken(token)) {
			throw new CustomException(CustomErrorCode.UNAUTHORIZED);
		}

		Long kakaoId = jwtUtil.getKakaoId(token);
		Member member = memberService.findByKakaoIdOrThrow(kakaoId);

		MemberDto.MemberInfoDto memberInfoDto = MemberDto.MemberInfoDto.builder()
			.kakaoId(member.getKakaoId())
			.email(member.getEmail())
			.role(member.getRole())
			.build();

		String accessToken = jwtUtil.createAccessToken(memberInfoDto);

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(token)
			.build();
	}

	public LoginResponse socialLogin(LoginRequest loginRequest) throws LoginException {
		boolean isNewMember = false;

		String kakaoAccessToken = oAuth.requestKakaoAccessToken(loginRequest);

		UserInfoGetDto userInfoGetDto = oAuth.getUserInfoFromKakao(kakaoAccessToken);

		Optional<Member> memberOptional = memberService.findByKakaoId(userInfoGetDto.getKakaoId());

		Member member;

		if (memberOptional.isPresent()) {
			member = memberOptional.get();

			// 기존 회원이지만, 회원가입 미완료 상태 (UNREGISTER)면 새 회원으로 판단
			if (Role.UNREGISTER.equals(member.getRole())) {
				isNewMember = true;
			}
		} else {
			member = Member.builder()
				.email(userInfoGetDto.getEmail())
				.name(userInfoGetDto.getName())
				.phone(userInfoGetDto.getPhone())
				.kakaoId(userInfoGetDto.getKakaoId())
				.birthday(userInfoGetDto.getBirthday())
				.profileImage(userInfoGetDto.getPicture())
				.role(Role.UNREGISTER)
				.build();

			memberService.save(member);
			isNewMember = true;
		}

		MemberDto.MemberInfoDto memberInfoDto = MemberDto.MemberInfoDto.builder()
			.kakaoId(member.getKakaoId())
			.email(member.getEmail())
			.role(member.getRole())
			.build();

		String accessToken = jwtUtil.createAccessToken(memberInfoDto);
		String refreshToken = jwtUtil.createRefreshToken(memberInfoDto);

		return LoginResponse.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.isNewMember(isNewMember)
			.build();
	}
}
