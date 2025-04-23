package com.mangoboss.app.domain.service.staff;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffService staffService;

    @Test
    void staff_엔티티를_만들_수_있다(){
        //given
        UserEntity user = mock(UserEntity.class);
        StoreEntity store = mock(StoreEntity.class);
        StaffEntity staff = mock(StaffEntity.class);
        when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

        //when
        StaffEntity result = staffService.createStaff(user,store);

        //then
        assertThat(result).isEqualTo(staff);
    }

    @Test
    void 이미_매장에_가입한_알바생이면_에러를_던진다(){
        //given
        UserEntity user = mock(UserEntity.class);
        StoreEntity store = mock(StoreEntity.class);
        when(staffRepository.existsByUserIdAndStoreId(any(Long.class), any(Long.class))).thenReturn(true);

        //when
        //then
        Assertions.assertThatThrownBy(()-> staffService.createStaff(user,store))
                .isInstanceOf(CustomException.class)
                .hasMessage(CustomErrorInfo.ALREADY_JOIN_STAFF.getMessage());
    }

    @Test
    void store예_속해있는_staff를_얻을_수_있다(){
        //given
        Long staffId = 1L;
        Long storeId = 1L;
        StaffEntity staff = mock(StaffEntity.class);
        when(staffRepository.getByIdAndStoreId(any(Long.class), any(Long.class))).thenReturn(staff);

        //when
        StaffEntity result = staffService.getStaffBelongsToStore(staffId,storeId);
        //then
        assertThat(result).isEqualTo(staff);
    }
}