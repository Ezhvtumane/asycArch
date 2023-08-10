package com.georgyorlov.task.service;

import com.georgyorlov.task.entity.TaskEntity;
import com.georgyorlov.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskEntity createTaskEntity(String text) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setDescription(text);

        return save(taskEntity);
    }

    public TaskEntity assignTask() {
        return null;
    }

    @Transactional
    public TaskEntity save(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    public TaskEntity findById(Long id) {
        return taskRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + id));
    }

}
