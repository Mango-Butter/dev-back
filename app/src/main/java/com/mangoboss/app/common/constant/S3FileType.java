package com.mangoboss.app.common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum S3FileType {
    CONTRACT_PDF("contracts/", ContentType.PDF),
    SIGNATURE_IMAGE("signatures/", ContentType.PNG);

    private final String folder;
    private final ContentType contentType;
}