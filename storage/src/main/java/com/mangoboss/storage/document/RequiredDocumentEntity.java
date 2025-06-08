package com.mangoboss.storage.document;

import com.mangoboss.storage.BaseTimeEntity;
import com.mangoboss.storage.store.StoreEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "required_document")
public class RequiredDocumentEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "required_document_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private StoreEntity store;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "is_required", nullable = false)
    private boolean isRequired;

    @Builder
    private RequiredDocumentEntity(final StoreEntity store, final DocumentType documentType, final boolean isRequired) {
        this.store = store;
        this.documentType = documentType;
        this.isRequired = isRequired;
    }

    public static RequiredDocumentEntity init(final StoreEntity store, final DocumentType documentType) {
        return RequiredDocumentEntity.builder()
                .store(store)
                .documentType(documentType)
                .isRequired(false)
                .build();
    }

    public void updateRequiredStatus(final boolean isRequired) {
        this.isRequired = isRequired;
    }
}