package com.georgyorlov.task.service.kafka;


import com.georgyorlov.avro.task.v1.TaskCompleted;
import com.georgyorlov.avro.task.v1.TaskCreated;
import com.georgyorlov.avro.task.v2.TaskStreaming;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTaskStreamingEventV1(com.georgyorlov.avro.task.v1.TaskStreaming taskStreamingEventData) {
        String topicName = "task-streaming";
        log.info("sendTaskStreamingEvent {} to topic {}", taskStreamingEventData, topicName);
        kafkaTemplate.send(topicName, taskStreamingEventData);
    }

    public void sendTaskStreamingEvent(TaskStreaming taskStreamingEventData) {
        String topicName = "task-streaming";
        log.info("sendTaskStreamingEvent {} to topic {}", taskStreamingEventData, topicName);
        kafkaTemplate.send(topicName, taskStreamingEventData);
    }

    public void sendTaskCreatedEvent(TaskCreated taskAssignEventData) {
        String topicName = "task-created";
        log.info("sendTaskCreatedEvent {} to topic {}", taskAssignEventData, topicName);
        kafkaTemplate.send(topicName, taskAssignEventData);
    }

    public void sendTaskCompletedEvent(TaskCompleted taskDoneEventData) {
        String topicName = "task-completed";
        log.info("sendTaskCompletedEvent {} to topic {}", taskDoneEventData, topicName);
        kafkaTemplate.send(topicName, taskDoneEventData);
    }

}
