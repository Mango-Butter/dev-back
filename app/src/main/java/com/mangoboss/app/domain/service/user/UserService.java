package com.mangoboss.app.domain.service.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.UserRepository;
import com.mangoboss.app.dto.user.KakaoUserInfo;
import com.mangoboss.storage.user.Role;
import com.mangoboss.storage.user.UserEntity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserEntity getUserById(final Long userId) {
        return userRepository.getById(userId);
    }

    public UserEntity getOrCreateUser(final KakaoUserInfo kakaoUserInfo) {
        return userRepository.findByKakaoId(kakaoUserInfo.kakaoId())
                .orElseGet(() -> createUserByKakao(kakaoUserInfo));
    }

    public UserEntity createUserByKakao(final KakaoUserInfo kakaoUserInfo) {
        final UserEntity userEntity = kakaoUserInfo.toEntity(Role.UNASSIGNED);
        return userRepository.save(userEntity);
    }

    public void signUp(final UserEntity user, final Role role) {
        if (user.getRole() != Role.UNASSIGNED) {
            throw new CustomException(CustomErrorInfo.ALREADY_SIGNED_UP);
        }
        user.assignRole(role);
    }
}
