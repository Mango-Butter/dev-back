package com.mangoboss.storage.faq;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "faq")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FaqEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faq_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FaqCategory category;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    private String answer;

    @Builder
    public FaqEntity(FaqCategory category, String question, String answer) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }

    public static FaqEntity create(final FaqCategory category, final String question, final String answer) {
        return FaqEntity.builder()
                .category(category)
                .question(question)
                .answer(answer)
                .build();
    }

    public void update(final String question, final String answer, final FaqCategory category) {
        this.question = question;
        this.answer = answer;
        this.category = category;
    }
}