package com.georgyorlov.audit.service.kafka;

import com.georgyorlov.audit.service.TaskService;
import com.georgyorlov.audit.service.TransactionService;
import com.georgyorlov.audit.service.UserService;
import com.georgyorlov.avro.task.v1.TaskCompleted;
import com.georgyorlov.avro.task.v1.TaskCreated;
import com.georgyorlov.avro.transaction.v1.TransactionCreated;
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
    private final TransactionService transactionService;

    @KafkaListener(topics = "user-streaming", groupId = "group-accounting")
    public void listenUserStreamingEventDTO(ConsumerRecord<String, UserStreaming> record) {
        log.info("[listenUserStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        userService.createOrUpdateFromUserStreaming(record.value());
    }

    @KafkaListener(topics = "transaction-created", groupId = "group-accounting")
    public void listenTransactionCreatedEventDTO(ConsumerRecord<String, TransactionCreated> record) {
        log.info("[listenTransactionCreatedEventDTO] Received Messasge in group - group-id: {}", record.value());
        transactionService.createOrUpdateFromTransactionStreaming(record.value());
    }

    @KafkaListener(topics = "task-streaming", groupId = "group-accounting")
    public void listenTaskStreamingEventDTO(ConsumerRecord<String, Object> record) {
        log.info("[listenTaskStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        if (record.value() instanceof com.georgyorlov.avro.task.v1.TaskStreaming) {
            taskService.createOrUpdateFromTaskStreamingV1((com.georgyorlov.avro.task.v1.TaskStreaming) record.value());
        }
    }

    @KafkaListener(topics = "task-streaming", groupId = "group-accounting")
    public void listenTaskStreamingEventDTOV2(ConsumerRecord<String, Object> record) {
        log.info("[listenTaskStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        if (record.value() instanceof com.georgyorlov.avro.task.v1.TaskStreaming) {
            taskService.createOrUpdateFromTaskStreamingV2((com.georgyorlov.avro.task.v2.TaskStreaming) record.value());
        }
    }

    @KafkaListener(topics = "task-created", groupId = "group-accounting")
    public void listenTaskCreatedEventDTO(ConsumerRecord<String, TaskCreated> record) {
        log.info("[listenTaskCreatedEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.taskCreated(record.value());
    }

    @KafkaListener(topics = "task-completed", groupId = "group-accounting")
    public void listenTaskCompletedEventDTO(ConsumerRecord<String, TaskCompleted> record) {
        log.info("[listenTaskCompletedEventDTO] Received Messasge in group - group-id: {}", record.value());
        taskService.taskCompleted(record.value());
    }
}
