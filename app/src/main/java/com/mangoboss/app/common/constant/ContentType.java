package com.mangoboss.app.common.constant;

import com.mangoboss.app.common.exception.CustomErrorInfo;
import com.mangoboss.app.common.exception.CustomException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum ContentType {
    PDF("application/pdf", "pdf"),
    PNG("image/png", "png"),
    JPEG("image/jpeg", "jpg"),
    HEIC("image/heic", "heic");

    private final String mimeType;
    private final String extension;

    public static String getExtensionByMimeType(final String mimeType) {
        return Stream.of(PDF, PNG, JPEG, HEIC)
                .filter(ct -> ct.getMimeType().equals(mimeType))
                .findFirst()
                .map(ContentType::getExtension)
                .orElseThrow(() -> new CustomException(CustomErrorInfo.UNSUPPORTED_FILE_TYPE));
    }
}