package com.mangoboss.app.domain;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mangoboss.app.exception.CustomErrorCode;
import com.mangoboss.app.exception.CustomException;
import com.mangoboss.storage.User;
import com.mangoboss.storage.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
	private final UserRepository userRepository;

	public User findByKakaoIdOrThrow(Long kakaoId) {
		return userRepository.findByKakaoId(kakaoId)
			.orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
	}

	public Optional<User> findByKakaoId(Long kakaoId) {
		return userRepository.findByKakaoId(kakaoId);
	}

	public void save(User user) {
		userRepository.save(user);
	}
}
