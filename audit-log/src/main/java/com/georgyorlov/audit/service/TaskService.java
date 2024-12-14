package com.georgyorlov.audit.service;

import com.georgyorlov.audit.entity.TaskEntity;
import com.georgyorlov.audit.entity.TaskStatus;
import com.georgyorlov.audit.repository.TaskRepository;
import com.georgyorlov.avro.task.v1.TaskCompleted;
import com.georgyorlov.avro.task.v1.TaskCreated;
import com.georgyorlov.avro.task.v2.TaskStreaming;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;


    public void createOrUpdateFromTaskStreamingV1(com.georgyorlov.avro.task.v1.TaskStreaming taskStreaming) {
        //
    }

    public void createOrUpdateFromTaskStreamingV2(TaskStreaming taskStreaming) {
        taskRepository.findByPublicId(UUID.fromString(taskStreaming.getPublicId().toString()))
            .ifPresentOrElse(
                taskEntity -> {
                    updateTask(taskEntity, taskStreaming);
                },
                () -> {
                    createAndSaveTaskEntity(taskStreaming);
                }
            );
    }

    private void updateTask(TaskEntity taskEntity, TaskStreaming taskStreaming) {
        taskEntity.setTitle(taskStreaming.getTitle().toString());
        taskEntity.setJiraId(taskStreaming.getJiraId().toString());
        save(taskEntity);
    }

    private void createAndSaveTaskEntity(TaskStreaming taskStreaming) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setTitle(taskStreaming.getTitle().toString());
        taskEntity.setJiraId(taskStreaming.getJiraId().toString());
        taskEntity.setPublicId(UUID.fromString(taskStreaming.getPublicId().toString()));
        taskEntity.setCostAssigning(taskStreaming.getCostAssigning());
        taskEntity.setCostCompleting(taskStreaming.getCostCompleting());
        taskEntity.setTaskStatus(TaskStatus.IN_PROGRESS);
        save(taskEntity);
    }

    private TaskEntity save(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }


    private TaskEntity findByPublicId(UUID publicId) {
        return taskRepository.findByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + publicId));
        //fix me if not found but taskAssign or taskDone event
    }

    @Transactional
    public void taskCreated(TaskCreated taskAssign) {
        TaskEntity taskEntity = findByPublicId(UUID.fromString(taskAssign.getTaskPublicId().toString()));
        taskEntity.setUserPublicId(UUID.fromString(taskAssign.getUserPublicId().toString()));
        save(taskEntity);
        //logic for debit from popug

    }

    @Transactional
    public void taskCompleted(TaskCompleted taskDone) {

        TaskEntity taskEntity = findByPublicId(UUID.fromString(taskDone.getTaskPublicId().toString()));
        taskEntity.setUserPublicId(UUID.fromString(taskDone.getUserPublicId().toString()));
        taskEntity.setTaskStatus(TaskStatus.DONE);
        save(taskEntity);
        //logic for credit to popug

    }
}
