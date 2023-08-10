package com.georgyorlov.task.service.kafka;

import com.georgyorlov.task.dto.kafka.UserCreatedEventDTO;
import com.georgyorlov.task.dto.kafka.UserUpdatedEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaListenerService {

    @KafkaListener(topics = "example", groupId = "group-id")
    public void listen(String message) {
        System.out.println("Received Messasge in group - group-id: " + message);
    }

    @KafkaListener(topics = "user-streaming", groupId = "group-id")
    public void listenUserCreatedEventDTO(UserCreatedEventDTO userCreatedEventDTO) {
        log.info("Received Messasge in group - group-id: {}", userCreatedEventDTO);
    }

    @KafkaListener(topics = "user-streaming", groupId = "group-id")
    public void listenUserUpdatedEventDTO(UserUpdatedEventDTO userUpdatedEventDTO) {
        log.info("Received Messasge in group - group-id: {}", userUpdatedEventDTO);
    }

}
