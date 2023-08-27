package com.georgyorlov.task.service.kafka;


import com.georgyorlov.avro.user.v1.UserStreaming;
import com.georgyorlov.task.service.UserService;
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

    @KafkaListener(topics = "user-streaming", groupId = "group-task-tracker")
    public void listenUserStreamingEventDTO(ConsumerRecord<String, UserStreaming> record) {
        log.info("[listenUserStreamingEventDTO] Received Messasge in group - group-id: {}", record.value());
        userService.createOrUpdateFromUserStreaming(record.value());
    }
}
