package com.georgyorlov.auth.service.kafka;

import com.georgyorlov.avro.schema.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserStreamingEvent(User user, String topicName) {
        log.info("sendUserCreatedEvent {} to topic {}", user, topicName);
        kafkaTemplate.send(topicName, user);
    }
}
