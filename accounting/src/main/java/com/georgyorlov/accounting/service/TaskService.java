package com.georgyorlov.accounting.service;

import com.georgyorlov.accounting.entity.TaskEntity;
import com.georgyorlov.accounting.entity.TaskStatus;
import com.georgyorlov.accounting.repository.TaskRepository;
import com.georgyorlov.avro.schema.TaskAssign;
import com.georgyorlov.avro.schema.TaskDone;
import com.georgyorlov.avro.schema.TaskStreaming;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TransactionService transactionService;

    public void createOrUpdateFromTaskStreaming(TaskStreaming taskStreaming) {
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
        taskEntity.setDescription(taskStreaming.getDescription().toString());
        save(taskEntity);
    }

    private void createAndSaveTaskEntity(TaskStreaming taskStreaming) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setDescription(taskStreaming.getDescription().toString());
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
    public void taskAssign(TaskAssign taskAssign) {
        TaskEntity taskEntity = findByPublicId(UUID.fromString(taskAssign.getTaskPublicId().toString()));
        taskEntity.setUserPublicId(UUID.fromString(taskAssign.getUserPublicId().toString()));
        save(taskEntity);
        //logic for debit from popug

        transactionService.createTransactionEntity(taskEntity.getPublicId(),
                                                   taskEntity.getUserPublicId(),
                                                   0L,
                                                   taskEntity.getCostAssigning().longValue()
        );
    }

    @Transactional
    public void taskComplete(TaskDone taskDone) {
        TaskEntity taskEntity = findByPublicId(UUID.fromString(taskDone.getTaskPublicId().toString()));
        taskEntity.setUserPublicId(UUID.fromString(taskDone.getUserPublicId().toString()));
        taskEntity.setTaskStatus(TaskStatus.DONE);
        save(taskEntity);
        //logic for credit to popug

        transactionService.createTransactionEntity(taskEntity.getPublicId(),
                                                   taskEntity.getUserPublicId(),
                                                   taskEntity.getCostCompleting().longValue(),
                                                   0L
        );
    }
}
