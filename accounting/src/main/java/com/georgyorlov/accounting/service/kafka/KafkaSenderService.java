package com.georgyorlov.accounting.service.kafka;

import com.georgyorlov.avro.transaction.v1.TransactionCreated;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaSenderService {


    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendTransactionCreatedEvent(TransactionCreated transactionCreated) {
        String topicName = "transaction-created";
        log.info("sendTransactionCreatedEvent {} to topic {}", transactionCreated, topicName);

        List<Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("version", "v1".getBytes()));

        ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, null, "transaction", transactionCreated, headers);

        kafkaTemplate.send(record);
    }

    //cud событие таска - task-streaming?
    //be событие task.lyfecycle?
    // task public id + old user public id + new user public id + type(assign/done).??
    //
}
