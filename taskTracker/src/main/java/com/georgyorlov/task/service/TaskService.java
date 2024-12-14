package com.georgyorlov.task.service;

import com.georgyorlov.avro.task.v1.TaskCompleted;
import com.georgyorlov.avro.task.v1.TaskCreated;
import com.georgyorlov.avro.task.v2.TaskStreaming;
import com.georgyorlov.task.dto.TaskCreateDTO;
import com.georgyorlov.task.entity.TaskEntity;
import com.georgyorlov.task.entity.TaskStatus;
import com.georgyorlov.task.repository.TaskRepository;
import com.georgyorlov.task.service.kafka.KafkaSenderService;
import java.util.Arrays;
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

    private static final List<String> validation = Arrays.asList("[", "]"); //props

    @Transactional
    public TaskEntity createTaskEntity(TaskCreateDTO taskCreateDTO) {
        if (validation.stream().anyMatch(s -> taskCreateDTO.getTitle().contains(s))) {
            throw new RuntimeException("Fix me");
        }

        Random random = new Random();

        TaskEntity taskEntity = new TaskEntity();
        taskEntity.setPublicId(UUID.randomUUID());
        taskEntity.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskEntity.setTitle(taskCreateDTO.getTitle());
        taskEntity.setJiraId(taskCreateDTO.getJiraId());
        taskEntity.setUserPublicId(userService.getRandomWorkerPublicId());
        taskEntity.setCostAssigning(random.nextInt(20 - 10) + 10); //-?
        taskEntity.setCostCompleting(random.nextInt(40 - 20) + 20);
        TaskEntity savedTask = save(taskEntity);

        TaskStreaming taskStreamingEventData = createTaskStreamingEventData(savedTask);
        kafkaSenderService.sendTaskStreamingEvent(taskStreamingEventData);

        TaskCreated taskAssignEventData = createTaskAssignEventData(taskEntity.getPublicId(), taskEntity.getUserPublicId());
        kafkaSenderService.sendTaskCreatedEvent(taskAssignEventData);
        return savedTask;
    }

    public void shuffleTasks() {
        findAllTasksInProgress()
            .forEach(this::setNewWorkerAndSendTaskEvent);
    }

    @Transactional
    public TaskEntity doneTask(UUID publicId) {
        TaskEntity byPublicId = findByPublicId(publicId);
        byPublicId.setTaskStatus(TaskStatus.DONE);
        TaskEntity savedTask = save(byPublicId);

        final TaskCompleted createTaskDoneEventData = createTaskDoneEventData(savedTask.getPublicId(), savedTask.getUserPublicId());
        kafkaSenderService.sendTaskCompletedEvent(createTaskDoneEventData);

        return savedTask;
    }

    @Async
    @Transactional
    public void setNewWorkerAndSendTaskEvent(TaskEntity task) {
        final UUID randomWorkerPublicId = userService.getRandomWorkerPublicId();

        task.setUserPublicId(randomWorkerPublicId);
        save(task);

        //move aout from transaction?
        sendTaskAssignedEvent(task);
    }

    @Async
    public void sendTaskAssignedEvent(TaskEntity task) {
        TaskCreated taskCreatedEventData = createTaskAssignEventData(task.getPublicId(), task.getUserPublicId());
        kafkaSenderService.sendTaskCreatedEvent(taskCreatedEventData);
    }

    private TaskCreated createTaskAssignEventData(UUID taskPublicId, UUID userPublicId) {
        return TaskCreated.newBuilder()
            .setTaskPublicId(taskPublicId.toString())
            .setUserPublicId(userPublicId.toString())
            .build();
    }

    private TaskCompleted createTaskDoneEventData(UUID taskPublicId, UUID userPublicId) {
        return TaskCompleted.newBuilder()
            .setTaskPublicId(taskPublicId.toString())
            .setUserPublicId(userPublicId.toString())
            .build();
    }

    private TaskStreaming createTaskStreamingEventData(TaskEntity taskEntity) {
        return TaskStreaming.newBuilder()
            .setPublicId(taskEntity.getPublicId().toString())
            .setTitle(taskEntity.getTitle())
            .setJiraId(taskEntity.getJiraId())
            .setCostAssigning(taskEntity.getCostAssigning())
            .setCostCompleting(taskEntity.getCostCompleting())
            .build();
    }

    private TaskEntity save(TaskEntity taskEntity) {
        return taskRepository.save(taskEntity);
    }

    private List<TaskEntity> findAllTasksInProgress() {
        return taskRepository.findByTaskStatus(TaskStatus.IN_PROGRESS);
    }

    private TaskEntity findByPublicId(UUID publicId) {
        return taskRepository.findByPublicId(publicId)
            .orElseThrow(() -> new RuntimeException("No entity found by id: " + publicId));
    }
}
