package com.georgyorlov.config;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    SchemaRegistryClient getSchemaRegistryClient() {
        return new CachedSchemaRegistryClient(
            "http://localhost:8081",
            5
        );
    }
}
