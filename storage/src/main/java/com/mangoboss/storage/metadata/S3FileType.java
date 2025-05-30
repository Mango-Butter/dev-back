package com.mangoboss.storage.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3FileType {
    CONTRACT("contracts/"),
    SIGNATURE("signatures/"),
    DOCUMENT("documents/"),
    PAYSLIP("payslips/"),
    TASK_REFERENCE("tasks/reference/"),
    TASK_REPORT("tasks/report/"),
    WORK_REPORT("work-reports/");

    private final String folder;
}
