package com.georgyorlov.accounting.service.kafka;

import com.georgyorlov.accounting.service.TaskService;
import com.georgyorlov.accounting.service.UserService;
import com.georgyorlov.avro.schema.TaskAssign;
import com.georgyorlov.avro.schema.TaskDone;
import com.georgyorlov.avro.schema.TaskStreaming;
import com.georgyorlov.avro.schema.User;
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
    public void listenUserStreamingEventDTO(ConsumerRecord<String, User> record) {
        log.info("[listenUserStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        userService.createOrUpdateFromUserStreaming(record.value());
    }

    @KafkaListener(topics = "task-streaming", groupId = "group-accounting")
    public void listenTaskStreamingEventDTO(ConsumerRecord<String, TaskStreaming> record) {
        log.info("[listenTaskStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.createOrUpdateFromTaskStreaming(record.value());
    }

    @KafkaListener(topics = "task-streaming-v2", groupId = "group-accounting")
    public void listenTaskStreamingEventDTOV2(ConsumerRecord<String, TaskStreaming> record) {
        log.info("[listenTaskStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.createOrUpdateFromTaskStreaming(record.value());
    }

    @KafkaListener(topics = "task-assigned", groupId = "group-accounting")
    public void listenTaskAssignEventDTO(ConsumerRecord<String, TaskAssign> record) {
        log.info("[listenTaskAssignEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.taskAssign(record.value());
    }

    @KafkaListener(topics = "task-done", groupId = "group-accounting")
    public void listenTaskDoneEventDTO(ConsumerRecord<String, TaskDone> record) {
        log.info("[listenTaskDoneEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.taskComplete(record.value());
    }
}
