package com.mangoboss.storage.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3FileType {
    CONTRACT("contracts/"),
    SIGNATURE("signatures/"),
    DOCUMENT("documents/"),
    PAYSLIP("payslips/");

    private final String folder;
}
