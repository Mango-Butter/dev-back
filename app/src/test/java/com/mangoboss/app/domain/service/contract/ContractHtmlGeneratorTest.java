package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.contract.request.ContractData;
import com.mangoboss.app.dto.contract.request.WorkSchedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContractHtmlGeneratorTest {

    private ContractHtmlGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new ContractHtmlGenerator();

        ReflectionTestUtils.setField(generator, "contractTemplatePath", "templates/contract-template.html");
    }

    private ContractData createSampleContractData() {
        WorkSchedule schedule = new WorkSchedule(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(18, 0));
        return ContractData.builder()
                .contractName("근로계약서")
                .storeName("망고가게")
                .staffName("홍길동")
                .contractStart(LocalDate.of(2024, 1, 1))
                .contractEnd(LocalDate.of(2024, 12, 31))
                .bossName("김사장")
                .storeAddress("서울특별시 중구")
                .duty("서빙 및 청소")
                .workSchedules(List.of(schedule))
                .hourlyWage(10000)
                .businessNumber("123-45-67890")
                .staffPhone("010-1234-5678")
                .build();
    }

    @Test
    void 보스_서명이_포함된_HTML을_정상적으로_생성한다() {
        ContractData data = createSampleContractData();
        String result = generator.generateHtmlWithBossSignature(data, "base64-boss-signature");

        assertThat(result).contains("base64-boss-signature");
        assertThat(result).contains(data.staffName());
    }

    @Test
    void 보스_알바_서명이_모두_포함된_HTML을_정상적으로_생성한다() {
        ContractData data = createSampleContractData();
        String result = generator.generateHtmlWithStaffSignature(data, "base64-boss-signature", "base64-staff-signature");

        assertThat(result).contains("base64-boss-signature");
        assertThat(result).contains("base64-staff-signature");
    }

    @Test
    void HTML_생성_중_예외가_발생하면_보스_서명_예외를_던진다() {
        ContractHtmlGenerator faultyGenerator = new ContractHtmlGenerator();

        assertThrows(CustomException.class, () ->
                ReflectionTestUtils.invokeMethod(faultyGenerator, "generateHtmlWithBossSignature", createSampleContractData(), "base64")
        );
    }
}