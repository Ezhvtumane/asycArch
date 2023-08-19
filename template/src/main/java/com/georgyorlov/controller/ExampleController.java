package com.georgyorlov.controller;

import com.georgyorlov.entity.ExampleEntity;
import com.georgyorlov.service.ExampleService;
import com.georgyorlov.service.kafka.ExampleStreamProducer;
import com.georgyorlov.service.kafka.KafkaSenderService;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/example")
@RequiredArgsConstructor
public class ExampleController {

    private final ExampleService exampleService;
    private final KafkaSenderService kafkaSenderService;
    private final ExampleStreamProducer exampleStreamProducer;

    @PostMapping
    public ExampleEntity createExampleEntity(@RequestBody String text) {
        return exampleService.createAndSaveExampleEntity(text);
    }

    @GetMapping("/{id}")
    public ExampleEntity findById(@PathVariable("id") Long id) {
        return exampleService.findById(id);
    }

    @PostMapping("/kafka-test")
    public ResponseEntity kafkaTest(@RequestBody String text) throws RestClientException, IOException {
        exampleStreamProducer.produceEmployeeDetails(0, "test", "test");
        return ResponseEntity.ok().build();
    }
}
