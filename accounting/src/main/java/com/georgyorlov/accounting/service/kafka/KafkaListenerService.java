package com.georgyorlov.accounting.service.kafka;

import com.georgyorlov.accounting.service.TaskService;
import com.georgyorlov.accounting.service.UserService;
import com.georgyorlov.avro.task.v1.TaskCompleted;
import com.georgyorlov.avro.task.v1.TaskCreated;
import com.georgyorlov.avro.task.v2.TaskStreaming;
import com.georgyorlov.avro.user.v1.UserStreaming;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaListenerService {

    private final UserService userService;
    private final TaskService taskService;

    @KafkaListener(topics = "user-streaming", groupId = "group-accounting")
    public void listenUserStreamingEventDTO(ConsumerRecord<String, UserStreaming> record) {
        log.info("[listenUserStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        userService.createOrUpdateFromUserStreaming(record.value());
    }

    @KafkaListener(topics = "task-streaming", groupId = "group-accounting")
    public void listenTaskStreamingEventDTO(ConsumerRecord<String, Object> record) {
        log.info("[listenTaskStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        if (record.value() instanceof com.georgyorlov.avro.task.v1.TaskStreaming) {
            com.georgyorlov.avro.task.v1.TaskStreaming taskStreaming = (com.georgyorlov.avro.task.v1.TaskStreaming) record.value();
            taskService.createOrUpdateFromTaskStreamingV1(taskStreaming);
        } else if (record.value() instanceof TaskStreaming) {
            TaskStreaming taskStreaming = (TaskStreaming) record.value();
            taskService.createOrUpdateFromTaskStreamingV2(taskStreaming);
        }
    }

    @KafkaListener(topics = "task-created", groupId = "group-accounting")
    public void listenTaskCreatedEventDTO(ConsumerRecord<String, TaskCreated> record) {
        log.info("[listenTaskCreatedEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.taskAssign(record.value());
    }

    @KafkaListener(topics = "task-completed", groupId = "group-accounting")
    public void listenTaskCompletedEventDTO(ConsumerRecord<String, TaskCompleted> record) {
        log.info("[listenTaskCompletedEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.taskComplete(record.value());
    }
}
