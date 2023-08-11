package com.georgyorlov.task.service.kafka;

import com.georgyorlov.task.dto.kafka.UserEventDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaListenerService {

    @KafkaListener(topics = "user-streaming", groupId = "group-id")
    public void listenUserStreamingEventDTO(UserEventDTO userEventDTO) {
        log.info("[listenUserCreatedEventDTO] Received Messasge in group - group-id: {}", userEventDTO);
    }

}