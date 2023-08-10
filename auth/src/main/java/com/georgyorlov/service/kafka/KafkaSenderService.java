package com.georgyorlov.service.kafka;

import com.georgyorlov.dto.kafka.UserCreatedEventDTO;
import com.georgyorlov.dto.kafka.UserUpdatedEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String msg) {
        kafkaTemplate.send("example", msg);
    }

    public void sendUserCreatedEvent(UserCreatedEventDTO userCreatedEventDTO, String topicName) {
        log.info("sendUserCreatedEvent {} to topic {}", userCreatedEventDTO, topicName);
        kafkaTemplate.send(topicName, userCreatedEventDTO);
    }

    public void sendUserUpdatedEvent(UserUpdatedEventDTO userUpdatedEventDTO, String topicName) {
        log.info("sendUserUpdatedEvent {} to topic {}", userUpdatedEventDTO, topicName);
        kafkaTemplate.send(topicName, userUpdatedEventDTO);
    }
}

