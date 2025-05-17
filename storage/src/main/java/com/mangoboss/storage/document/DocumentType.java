package com.mangoboss.storage.document;

import lombok.Getter;

@Getter
public enum DocumentType {
    RESIDENT_REGISTRATION("resident-registration/"), // 주민등록등본
    HEALTH_CERTIFICATE("health-certificate/"), // 보건증
    BANK_ACCOUNT("bank-account/"), // 통장 사본
    ID_CARD("identification/"); // 신분증 사본

    private final String folder;

    DocumentType(final String folder) {
        this.folder = folder;
    }

}