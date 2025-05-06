package com.mangoboss.storage.contract;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "contract")
public class ContractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contract_id")
    private Long id;

    @Column(nullable = false)
    private Long staffId;

    @Column(nullable = false)
    private String fileKey;

    @Column(nullable = false, length = 512)
    private String bossSignatureKey;

    @Column(length = 512)
    private String staffSignatureKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime bossSignedAt;

    private LocalDateTime staffSignedAt;

    @Lob
    @Column(name = "contract_data_json", columnDefinition = "MEDIUMTEXT", nullable = false)
    private String contractDataJson;

    @Builder
    private ContractEntity(final Long staffId, final String fileKey, final String bossSignatureKey,
                           final ContractStatus status, final LocalDateTime createdAt,
                           final LocalDateTime bossSignedAt, final LocalDateTime staffSignedAt,
                           final String contractDataJson) {
        this.staffId = staffId;
        this.fileKey = fileKey;
        this.bossSignatureKey = bossSignatureKey;
        this.status = status;
        this.createdAt = createdAt;
        this.bossSignedAt = bossSignedAt;
        this.staffSignedAt = staffSignedAt;
        this.contractDataJson = contractDataJson;
    }

    // 계약 생성 (초안 상태)
    public static ContractEntity create(final Long staffId, final String fileKey, final String contractDataJson,
                                        final LocalDateTime createdAt, final String bossSignatureKey, final LocalDateTime bossSignedAt) {
        return ContractEntity.builder()
                .staffId(staffId)
                .fileKey(fileKey)
                .contractDataJson(contractDataJson)
                .createdAt(createdAt)
                .bossSignatureKey(bossSignatureKey)
                .bossSignedAt(bossSignedAt)
                .status(ContractStatus.PENDING_STAFF_SIGNATURE)
                .build();
    }

    // 알바생 서명 완료 처리
    public ContractEntity completeStaffSign(final String signedFileKey, final String staffSignatureKey, final LocalDateTime signedAt) {
        this.fileKey = signedFileKey;
        this.staffSignatureKey = staffSignatureKey;
        this.staffSignedAt = signedAt;
        this.status = ContractStatus.COMPLETED;
        return this;
    }

}