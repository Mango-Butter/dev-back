package com.mangoboss.app.domain.service.contract;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import com.mangoboss.app.dto.contract.request.ContractData;
import com.mangoboss.app.dto.contract.request.WorkSchedule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ContractHtmlGenerator {

    @Value("${contract.template-path}")
    private String contractTemplatePath;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public String generateHtmlWithBossSignature(final ContractData contractData, final String bossSignatureBase64) {
        try {
            return fillTemplate(contractData, bossSignatureBase64, "");
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.BOSS_SIGNED_HTML_GENERATION_FAILED);
        }
    }

    public String generateHtmlWithStaffSignature(final ContractData contractData, final String bossSignatureBase64, final String staffSignatureBase64) {
        try {
            return fillTemplate(contractData, bossSignatureBase64, staffSignatureBase64);
        } catch (Exception e) {
            throw new CustomException(CustomErrorInfo.STAFF_SIGNED_HTML_GENERATION_FAILED);
        }
    }

    private String fillTemplate(final ContractData data, final String bossSignature, final String staffSignature) throws Exception {
        final String template = Files.readString(new ClassPathResource(contractTemplatePath).getFile().toPath());

        final Map<String, String> dataMap = new HashMap<>();
        dataMap.put("staffName", data.staffName());
        dataMap.put("staffPhone", data.staffPhone());
        dataMap.put("contractStart", String.valueOf(data.contractStart()));
        dataMap.put("contractEnd", String.valueOf(data.contractEnd()));
        dataMap.put("hourlyWage", data.hourlyWage().toString());
        dataMap.put("storeName", data.storeName());
        dataMap.put("storeAddress", data.storeAddress());
        dataMap.put("bossName", data.bossName());
        dataMap.put("businessNumber", data.businessNumber());
        dataMap.put("bossSignature", buildSignatureBase64Url(bossSignature));
        dataMap.put("staffSignature", buildSignatureBase64Url(staffSignature));
        dataMap.put("workSchedules", buildScheduleRows(data.workSchedules()));
        dataMap.put("duty", data.duty());

        String filledHtml = template;
        for (Map.Entry<String, String> entry : dataMap.entrySet()) {
            filledHtml = filledHtml.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        return filledHtml;
    }

    private String buildScheduleRows(final List<WorkSchedule> schedules) {
        return schedules.stream()
                .sorted(Comparator.comparing(WorkSchedule::dayOfWeek))
                .map(s -> String.format(
                        "<tr><td style='border:1px solid #000;'>%s</td><td style='border:1px solid #000;'>%s</td><td style='border:1px solid #000;'>%s</td></tr>",
                        s.dayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN),
                        s.startTime().format(TIME_FORMATTER),
                        s.endTime().format(TIME_FORMATTER)))
                .collect(Collectors.joining());
    }

    private String buildSignatureBase64Url(final String base64Signature) {
        return Optional.ofNullable(base64Signature)
                .filter(s -> !s.isBlank())
                .orElse("");
    }
}