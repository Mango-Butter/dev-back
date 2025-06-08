package com.mangoboss.app.dto.document.response;


import lombok.Builder;

@Builder
public record StaffDocumentStatusResponse(
        Long staffId,
        String staffName,
        boolean isSubmitted,
        Long documentId
) {
    public static StaffDocumentStatusResponse of(final Long staffId, final String staffName,
                                                 final boolean isSubmitted, final Long documentId) {
        return StaffDocumentStatusResponse.builder()
                .staffId(staffId)
                .staffName(staffName)
                .isSubmitted(isSubmitted)
                .documentId(documentId)
                .build();
    }
}
