package com.mangoboss.storage.contract;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContractTemplate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long storeId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contractTemplateDataJson;

    @Builder
    private ContractTemplate(final Long storeId, final String title, final String contractTemplateDataJson) {
        this.storeId = storeId;
        this.title = title;
        this.contractTemplateDataJson = contractTemplateDataJson;
    }

    public static ContractTemplate create(final Long storeId, final String title, final String contractTemplateDataJson) {
        return ContractTemplate.builder()
                .storeId(storeId)
                .title(title)
                .contractTemplateDataJson(contractTemplateDataJson)
                .build();
    }

    public ContractTemplate update(final String title, final String contractTemplateDataJson) {
        this.title = title;
        this.contractTemplateDataJson = contractTemplateDataJson;
        return this;
    }
}