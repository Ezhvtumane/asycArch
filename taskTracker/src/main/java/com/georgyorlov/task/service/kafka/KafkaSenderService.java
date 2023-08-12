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

    public void sendTaskStreamingEvent(TaskEventDTO taskEventDTO, String topicName) {
        log.info("sendTaskStreamingEvent {} to topic {}", taskEventDTO, topicName);
        kafkaTemplate.send(topicName, taskEventDTO);
    }
}
