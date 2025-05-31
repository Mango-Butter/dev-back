package com.mangoboss.app.api.facade.staff;

import com.mangoboss.app.domain.service.schedule.ScheduleService;
import com.mangoboss.app.domain.service.staff.StaffService;
import com.mangoboss.app.domain.service.store.StoreService;
import com.mangoboss.app.dto.staff.request.RegularGroupCreateRequest;
import com.mangoboss.app.dto.staff.request.StaffHourlyWageRequest;
import com.mangoboss.app.dto.staff.request.StaffWithholdingRequest;
import com.mangoboss.app.dto.staff.response.RegularGroupResponse;
import com.mangoboss.app.dto.staff.response.StaffHourlyWageResponse;
import com.mangoboss.app.dto.staff.response.StaffSimpleResponse;
import com.mangoboss.app.dto.staff.response.StaffWithholdingTypeResponse;
import com.mangoboss.storage.schedule.RegularGroupEntity;
import com.mangoboss.storage.staff.StaffEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BossStaffFacade {
    private final ScheduleService scheduleService;
    private final StaffService staffService;
    private final StoreService storeService;

    public void createRegularSchedules(final Long storeId, final Long staffId, final Long bossId, final List<RegularGroupCreateRequest> requestList) {
        storeService.isBossOfStore(storeId, bossId);
        requestList.forEach(request -> scheduleService.validateDate(
                request.startDate(), request.endDate(), request.startTime(), request.endTime())
        );
        final StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, staffId);
        final List<RegularGroupEntity> regularGroups = requestList.stream().map(request -> request.toEntity(staff)).toList();
        scheduleService.createRegularGroupAndSchedules(regularGroups, storeId);
    }

    public List<RegularGroupResponse> getRegularGroupsForStaff(final Long storeId, final Long staffId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return scheduleService.getRegularGroupsForStaff(staffId)
                .stream().map(RegularGroupResponse::fromEntity).toList();
    }

    public void terminateRegularGroup(final Long storeId, final Long bossId, final Long regularGroupId) {
        storeService.isBossOfStore(storeId, bossId);
        scheduleService.terminateRegularGroup(regularGroupId);
    }

    public List<StaffSimpleResponse> getStaffsForStore(final Long storeId, final Long boosId) {
        storeService.isBossOfStore(storeId, boosId);
        List<StaffEntity> staffs = staffService.getStaffsForStore(storeId);
        return staffs.stream().map(StaffSimpleResponse::fromEntity).toList();
    }

    public StaffSimpleResponse getStaffDetail(final Long storeId, final Long staffId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        final StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, staffId);
        return StaffSimpleResponse.fromEntity(staff);
    }

    public List<StaffWithholdingTypeResponse> getStaffWithholdingTypes(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return staffService.getStaffsForStore(storeId)
                .stream().map(StaffWithholdingTypeResponse::fromEntity).toList();
    }

    public void updateStaffWithholdingType(final Long storeId, final Long staffId, final Long bossId, StaffWithholdingRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        final StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, staffId);
        staffService.updateWithholdingType(staff, request.withholdingType());
    }

    public List<StaffHourlyWageResponse> getStaffWithHourlyWage(final Long storeId, final Long bossId) {
        storeService.isBossOfStore(storeId, bossId);
        return staffService.getStaffsForStore(storeId)
                .stream().map(StaffHourlyWageResponse::fromEntity).toList();
    }

    public void updateStaffHourlyWage(final Long storeId, final Long staffId, final Long bossId, final StaffHourlyWageRequest request) {
        storeService.isBossOfStore(storeId, bossId);
        final StaffEntity staff = staffService.validateStaffBelongsToStore(storeId, staffId);
        staffService.updateHourlyWage(staff, request.hourlyWage());
    }
}