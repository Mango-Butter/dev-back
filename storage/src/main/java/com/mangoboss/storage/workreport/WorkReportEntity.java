package com.mangoboss.storage.workreport;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "work_report")
public class WorkReportEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_report_id")
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(nullable = false)
    private String content;

    private String reportImageUrl;

    @Builder
    public WorkReportEntity(final Long storeId, final Long staffId, final String content, final String reportImageUrl) {
        this.storeId = storeId;
        this.staffId = staffId;
        this.content = content;
        this.reportImageUrl = reportImageUrl;
    }

    public static WorkReportEntity create(final Long storeId, final Long staffId, final String content, final String reportImageUrl) {
        return WorkReportEntity.builder()
                .storeId(storeId)
                .staffId(staffId)
                .content(content)
                .reportImageUrl(reportImageUrl)
                .build();
    }
}