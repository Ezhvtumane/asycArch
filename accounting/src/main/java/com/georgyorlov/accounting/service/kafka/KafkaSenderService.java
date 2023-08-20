package com.georgyorlov.accounting.service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {
/*

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTaskStreamingEvent(TaskStreaming taskStreamingEventData) {
        String topicName = "task-streaming";
        log.info("sendTaskStreamingEvent {} to topic {}", taskStreamingEventData, topicName);
        kafkaTemplate.send(topicName, taskStreamingEventData);
    }

    public void sendTaskDoneEvent(TaskDone taskDoneEventData) {
        String topicName = "task-lyfecycle";
        log.info("sendTaskLifecycleEvent {} to topic {}", taskDoneEventData, topicName);
        kafkaTemplate.send(topicName, taskDoneEventData);
    }

    public void sendTaskAssignEvent(TaskAssign taskAssignEventData) {
        String topicName = "task-lyfecycle";
        log.info("sendTaskLifecycleEvent {} to topic {}", taskAssignEventData, topicName);
        kafkaTemplate.send(topicName, taskAssignEventData);
    }
*/

    //cud событие таска - task-streaming?
    //be событие task.lyfecycle?
    // task public id + old user public id + new user public id + type(assign/done).??
    //
}
