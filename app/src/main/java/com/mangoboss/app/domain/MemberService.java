package com.mangoboss.app.domain;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mangoboss.app.exception.CustomErrorCode;
import com.mangoboss.app.exception.CustomException;
import com.mangoboss.storage.Member;
import com.mangoboss.storage.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
	private final MemberRepository memberRepository;

	public Member findByKakaoIdOrThrow(Long kakaoId) {
		return memberRepository.findByKakaoId(kakaoId)
			.orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
	}

	public Optional<Member> findByKakaoId(Long kakaoId) {
		return memberRepository.findByKakaoId(kakaoId);
	}

	public void save(Member member) {
		memberRepository.save(member);
	}
}
