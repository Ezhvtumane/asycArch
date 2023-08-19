package com.georgyorlov.service.kafka;

import com.georgyorlov.schema.Employee;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaListenerService {

    @KafkaListener(topics = "example", groupId = "group-id")
    public void listen(String message) {
        System.out.println("Received Messasge in group - group-id: " + message);
    }


    @KafkaListener(topics = "employee", groupId = "group_id")
    public void consume(ConsumerRecord<String, Employee> record) {
        log.info(String.format("Consumed message -> %s", record.value()));
    }

}
