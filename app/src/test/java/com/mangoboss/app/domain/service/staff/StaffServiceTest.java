package com.mangoboss.app.domain.service.staff;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.domain.repository.StaffRepository;
import com.mangoboss.storage.payroll.BankCode;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        UserEntity boss = mock(UserEntity.class);
        when(staffRepository.save(any(StaffEntity.class))).thenReturn(staff);

        //when
        StaffEntity result = staffService.createStaff(user,store,boss);

        //then
        assertThat(result).isEqualTo(staff);
    }

    @Test
    void 이미_매장에_가입한_알바생이면_에러를_던진다(){
        //given
        UserEntity user = mock(UserEntity.class);
        StoreEntity store = mock(StoreEntity.class);
        UserEntity boss = mock(UserEntity.class);
        when(staffRepository.existsByUserIdAndStoreId(any(Long.class), any(Long.class))).thenReturn(true);

        //when
        //then
        Assertions.assertThatThrownBy(()-> staffService.createStaff(user,store,boss))
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
        StaffEntity result = staffService.validateStaffBelongsToStore(staffId,storeId);
        //then
        assertThat(result).isEqualTo(staff);
    }

    @Test
    void staffId로_staff를_조회할_수_있다() {
        // given
        Long staffId = 10L;
        StaffEntity staff = mock(StaffEntity.class);
        when(staffRepository.getById(staffId)).thenReturn(staff);

        // when
        StaffEntity result = staffService.getStaffById(staffId);

        // then
        assertThat(result).isEqualTo(staff);
    }

    @Test
    void staff의_시급을_변경할_수_있다() {
        // given
        StaffEntity staff = mock(StaffEntity.class);
        Integer newWage = 12000;

        // when
        staffService.updateHourlyWage(staff, newWage);

        // then
        verify(staff).updateHourlyWage(newWage);
    }

    @Test
    void staff의_계좌를_등록할_수_있다() {
        // given
        StaffEntity staff = mock(StaffEntity.class);
        BankCode bankCode = BankCode.NH;
        String account = "123-456-7890";

        // when
        staffService.registerStaffAccount(staff, bankCode, account);

        // then
        verify(staff).registerAccount(bankCode, account);
    }

    @Test
    void userId로_직원목록을_조회할_수_있다() {
        // given
        Long userId = 1L;
        List<StaffEntity> staffs = List.of(mock(StaffEntity.class));
        when(staffRepository.findAllByUserId(userId)).thenReturn(staffs);

        // when
        List<StaffEntity> result = staffService.getStaffsByUserId(userId);

        // then
        assertThat(result).isEqualTo(staffs);
    }
}