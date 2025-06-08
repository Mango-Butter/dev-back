package com.mangoboss.storage.document;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "document")
public class DocumentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    @Column(nullable = false)
    private Long staffId;

    @Column(nullable = false)
    private Long storeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentType documentType;

    @Column(nullable = false)
    private String contentType;

    @Column(nullable = false, length = 512)
    private String fileKey;

    private LocalDate expiresAt;

    @Builder
    private DocumentEntity(final Long staffId, final Long storeId, final DocumentType documentType, final String contentType,
                           final String fileKey, final LocalDate expiresAt) {
        this.staffId = staffId;
        this.storeId = storeId;
        this.documentType = documentType;
        this.contentType = contentType;
        this.fileKey = fileKey;
        this.expiresAt = expiresAt;
    }

    public static DocumentEntity create(final Long staffId, final Long storeId, final DocumentType documentType, final String fileKey,
                                        final String contentType, final LocalDate expiresAt) {
        return DocumentEntity.builder()
                .staffId(staffId)
                .storeId(storeId)
                .documentType(documentType)
                .contentType(contentType)
                .fileKey(fileKey)
                .expiresAt(expiresAt)
                .build();
    }
}