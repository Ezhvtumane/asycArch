package com.georgyorlov.task.service.kafka;

import com.georgyorlov.avro.schema.TaskAssign;
import com.georgyorlov.avro.schema.TaskDone;
import com.georgyorlov.avro.schema.TaskStreaming;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTaskStreamingEvent(TaskStreaming taskStreamingEventData) {
        String topicName = "task-streaming";
        log.info("sendTaskStreamingEvent {} to topic {}", taskStreamingEventData, topicName);
        kafkaTemplate.send(topicName, taskStreamingEventData);
    }

    public void sendTaskAssignEvent(TaskAssign taskAssignEventData) {
        String topicName = "task-assigned";
        log.info("sendTaskLifecycleEvent {} to topic {}", taskAssignEventData, topicName);
        kafkaTemplate.send(topicName, taskAssignEventData);
    }

    public void sendTaskDoneEvent(TaskDone taskDoneEventData) {
        String topicName = "task-done";
        log.info("sendTaskLifecycleEvent {} to topic {}", taskDoneEventData, topicName);
        kafkaTemplate.send(topicName, taskDoneEventData);
    }

}
