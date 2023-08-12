package com.georgyorlov.task.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tasks")
public class TaskEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "public_id")
    private UUID publicId;

    @Column(name = "task_status")
    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

    @Column(name = "user_public_id")
    private UUID userPublicId;

    @Column(name = "cost_assigning")
    private Integer costAssaigning;

    @Column(name = "cost_completing")
    private Integer costCompleting;
/*
    @Column(name = "done_at")
    private Instant doneAt;
*/

}
