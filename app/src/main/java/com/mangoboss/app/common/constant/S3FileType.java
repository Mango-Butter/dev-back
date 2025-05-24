package com.mangoboss.app.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3FileType {
    CONTRACT("contracts/"),
    SIGNATURE("signatures/"),
    DOCUMENT("documents/"),
    TASK("tasks/");

    private final String folder;
}