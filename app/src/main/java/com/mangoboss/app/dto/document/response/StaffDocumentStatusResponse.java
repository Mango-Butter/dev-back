package com.mangoboss.app.dto.document.response;

public record StaffDocumentStatusResponse(
        Long staffId,
        String staffName,
        boolean isSubmitted,
        Long documentId
) {
    public static StaffDocumentStatusResponse of(Long staffId, String staffName, boolean isSubmitted, Long documentId) {
        return new StaffDocumentStatusResponse(staffId, staffName, isSubmitted, documentId);
    }
}