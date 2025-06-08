package com.mangoboss.storage.metadata;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
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

    public static Optional<String> getExtensionByMimeType(final String mimeType) {
        return Stream.of(PDF, PNG, JPEG, HEIC)
                .filter(ct -> ct.getMimeType().equals(mimeType))
                .findFirst()
                .map(ContentType::getExtension);
    }
}