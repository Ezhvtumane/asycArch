package com.georgyorlov.task.service;

import com.georgyorlov.task.dto.TaskCreateDTO;
import com.georgyorlov.task.dto.kafka.TaskEventDTO;
import com.georgyorlov.task.entity.TaskEntity;
import com.georgyorlov.task.entity.TaskStatus;
import com.georgyorlov.task.repository.TaskRepository;
import com.georgyorlov.task.service.kafka.KafkaSenderService;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final KafkaSenderService kafkaSenderService;
    private final UserService userService;

    @Transactional
    public TaskEntity createTaskEntity(TaskCreateDTO taskCreateDTO) {
        Random random = new Random();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setPublicId(UUID.randomUUID());
        taskEntity.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setDescription(taskCreateDTO.getDescription());
        taskEntity.setUserPublicId(userService.getRandomWorkerPublicId());
        taskEntity.setCostAssaigning(random.nextInt(20 - 10) + 10); //-?
        taskEntity.setCostCompleting(random.nextInt(40 - 20) + 20);
        TaskEntity savedTask = save(taskEntity);

        sendTaskStreamingEvent(savedTask);

        return savedTask;
    }

    public TaskEntity save(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    public List<TaskEntity> findAllTasksInProgress() {
        return taskRepository.findByTaskStatus(TaskStatus.IN_PROGRESS);
    }

    public TaskEntity findByPublicId(UUID publicId) {
        return taskRepository.findByPublicId(publicId)
            .orElseThrow();
    }

    @Async
    public void sendTaskStreamingEvent(TaskEntity task) {
        TaskEventDTO taskEventDTO = new TaskEventDTO();
        taskEventDTO.setDescription(task.getDescription());
        taskEventDTO.setPublicId(task.getPublicId());
        taskEventDTO.setUserPublicId(task.getUserPublicId());
        taskEventDTO.setCostAssaigning(task.getCostAssaigning());
        taskEventDTO.setCostCompleting(task.getCostCompleting());
        kafkaSenderService.sendTaskStreamingEvent(taskEventDTO, "task-streaming");
    }

    public void shuffleTasks() {
        findAllTasksInProgress()
            .forEach(this::setNewWorkerAndSendTaskEvent);
    }

    @Async
    @Transactional
    public void setNewWorkerAndSendTaskEvent(TaskEntity task) {
        final UUID randomWorkerPublicId = userService.getRandomWorkerPublicId();

        task.setUserPublicId(randomWorkerPublicId);
        save(task);

        sendTaskStreamingEvent(task);
    }

    @Transactional
    public TaskEntity doneTask(UUID publicId) {
        TaskEntity byPublicId = findByPublicId(publicId);
        byPublicId.setTaskStatus(TaskStatus.DONE);
        TaskEntity savedTask = save(byPublicId);
        sendTaskStreamingEvent(savedTask);
        return savedTask;
    }
}
