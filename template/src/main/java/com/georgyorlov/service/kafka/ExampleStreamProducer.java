package com.georgyorlov.service.kafka;

import com.georgyorlov.schema.Employee;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExampleStreamProducer {

    private final KafkaSenderService kafkaSenderService;
    private final SchemaRegistryClient schemaRegistryClient;

    public void produceEmployeeDetails(int empId, String firstName, String lastName) throws RestClientException, IOException {

        // creating employee details
        Employee employee = new Employee();
        employee.setId(empId);
        employee.setFirstName(firstName);
        //employee.setLastName(lastName);
        //employee.setKekLol("kek?");
        Schema schema = employee.getSchema();
        //Schema bySubjectAndId = schemaRegistryClient.getBySubjectAndId("employee-value", 3);
        //KafkaAvroSerializer kafkaAvroSerializer = new KafkaAvroSerializer();

        kafkaSenderService.sendMessage(employee);
    }
}
