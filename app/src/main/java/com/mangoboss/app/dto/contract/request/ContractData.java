package com.mangoboss.app.dto.contract.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mangoboss.storage.staff.StaffEntity;
import com.mangoboss.storage.store.StoreEntity;
import com.mangoboss.storage.user.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record ContractData(

        @NotBlank
        String contractName,

        @NotBlank
        String storeName,

        @NotBlank
        String staffName,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate contractStart,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate contractEnd,

        @NotBlank
        String bossName,

        @NotBlank
        String storeAddress,

        @NotBlank
        String duty,

        @NotNull
        List<WorkSchedule> workSchedules,

        @NotNull
        Integer hourlyWage,

        @NotBlank
        String businessNumber,

        @NotBlank
        String staffPhone
) {
    public static ContractData of(ContractDataInput input, UserEntity boss, StoreEntity store, StaffEntity staff) {
        return ContractData.builder()
                .contractName(input.contractName())
                .storeName(store.getName())
                .staffName(staff.getName())
                .contractStart(input.contractStart())
                .contractEnd(input.contractEnd())
                .bossName(boss.getName())
                .storeAddress(store.getAddress())
                .duty(input.duty())
                .workSchedules(input.workSchedules())
                .hourlyWage(input.hourlyWage())
                .businessNumber(store.getBusinessNumber())
                .staffPhone(staff.getUser().getPhone())
                .build();
    }
}
