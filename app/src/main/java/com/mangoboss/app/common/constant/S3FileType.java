package com.mangoboss.app.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3FileType {
    CONTRACT("contracts/"),
    SIGNATURE("signatures/"),
    DOCUMENT("documents/"),
    TASK_REFERENCE("tasks/reference/"),
    TASK_REPORT("tasks/report/");

    private final String folder;
}