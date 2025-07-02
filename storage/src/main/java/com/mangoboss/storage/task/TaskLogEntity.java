package com.mangoboss.storage.task;

import com.mangoboss.storage.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "task_log",
        indexes = {
                @Index(name = "idx_task_log_task", columnList = "task_id"),
        }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskLogEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_log_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private TaskEntity task;

    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @Column(name = "task_log_image_url")
    private String taskLogImageUrl;

    @Builder
    public TaskLogEntity(TaskEntity task, Long staffId, String taskLogImageUrl) {
        this.task = task;
        this.staffId = staffId;
        this.taskLogImageUrl = taskLogImageUrl;
    }

    public static TaskLogEntity create(final TaskEntity task, final Long staffId, final String taskLogImageUrl) {
        return TaskLogEntity.builder()
                .task(task)
                .staffId(staffId)
                .taskLogImageUrl(taskLogImageUrl)
                .build();
    }
}
