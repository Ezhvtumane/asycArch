package com.georgyorlov.accounting.repository;

import com.georgyorlov.accounting.entity.TaskEntity;
import com.georgyorlov.accounting.entity.TaskStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskEntity, Long> {

    Optional<TaskEntity> findByPublicId(UUID publicId);

    List<TaskEntity> findByTaskStatus(TaskStatus taskStatus);
}
