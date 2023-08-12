package com.georgyorlov.task.service.kafka;

import com.georgyorlov.task.dto.kafka.TaskEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTaskAssignedEvent(TaskEventDTO taskEventDTO) {
        String topicName = "task-assigned";
        log.info("sendTaskStreamingEvent {} to topic {}", taskEventDTO, topicName);
        kafkaTemplate.send(topicName, taskEventDTO);
    }

    public void sendTaskDoneEvent(TaskEventDTO taskEventDTO) {
        String topicName = "task-done";
        log.info("sendTaskStreamingEvent {} to topic {}", taskEventDTO, topicName);
        kafkaTemplate.send(topicName, taskEventDTO);
    }

}
