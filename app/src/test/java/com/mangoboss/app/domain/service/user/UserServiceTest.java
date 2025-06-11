package com.mangoboss.app.domain.service.user;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.UserRepository;
import com.mangoboss.app.dto.user.KakaoUserInfo;
import com.mangoboss.storage.user.Role;
import com.mangoboss.storage.user.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void 유저ID로_유저를_정상적으로_조회할_수_있다() {
        // given
        Long userId = 1L;
        UserEntity user = mock(UserEntity.class);
        given(userRepository.getById(userId)).willReturn(user);

        // when
        UserEntity result = userService.getUserById(userId);

        // then
        assertEquals(user, result);
    }

    @Test
    void 카카오ID가_존재하면_기존_유저를_반환해야_한다() {
        // given
        KakaoUserInfo kakaoInfo = KakaoUserInfo.create(123L, "test@example.com", "홍길동", "http://img", LocalDate.of(2000, 1, 1), "010-0000-0000");
        UserEntity existingUser = mock(UserEntity.class);
        given(userRepository.findByKakaoId(123L)).willReturn(Optional.of(existingUser));

        // when
        UserEntity result = userService.getOrCreateUser(kakaoInfo);

        // then
        assertEquals(existingUser, result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void 카카오ID가_존재하지_않으면_유저를_신규_생성해야_한다() {
        // given
        KakaoUserInfo kakaoInfo = KakaoUserInfo.create(456L, "new@example.com", "이순신", "http://img", LocalDate.of(1990, 5, 15), "010-1234-5678");
        UserEntity newUser = kakaoInfo.toEntity(Role.UNASSIGNED);

        given(userRepository.findByKakaoId(456L)).willReturn(Optional.empty());
        given(userRepository.save(any())).willReturn(newUser);

        // when
        UserEntity result = userService.getOrCreateUser(kakaoInfo);

        // then
        assertEquals(newUser, result);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void UNASSIGNED_상태의_유저는_역할을_정상적으로_부여받을_수_있다() {
        // given
        UserEntity user = mock(UserEntity.class);
        given(user.getRole()).willReturn(Role.UNASSIGNED);

        // when
        userService.signUp(user, Role.BOSS);

        // then
        verify(user).assignRole(Role.BOSS);
    }

    @Test
    void 이미_가입된_유저는_회원가입시_예외를_던져야_한다() {
        // given
        UserEntity user = mock(UserEntity.class);
        given(user.getRole()).willReturn(Role.BOSS);

        // when
        CustomException ex = assertThrows(CustomException.class,
                () -> userService.signUp(user, Role.STAFF)
        );

        // then
        assertEquals(CustomErrorInfo.ALREADY_SIGNED_UP, ex.getErrorCode());
    }
}