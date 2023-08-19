package com.georgyorlov.service.kafka;

import com.georgyorlov.schema.Employee;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class KafkaSenderService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendMessage(String msg) {
        kafkaTemplate.send("example", msg);
    }

    void sendMessage(Employee user) {
        this.kafkaTemplate.send("employee", user);
        log.info(String.format("Produced user -> %s", user));
    }

}

