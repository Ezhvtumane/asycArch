package com.georgyorlov.auth.config;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.converter.ByteArrayJsonMessageConverter;
import org.springframework.kafka.support.converter.JsonMessageConverter;

@Configuration
public class KafkaConfig {

    @Bean
    public JsonMessageConverter jsonMessageConverter() {
        return new ByteArrayJsonMessageConverter();
    }

    @Bean
    SchemaRegistryClient getSchemaRegistryClient() {
        return new CachedSchemaRegistryClient(
            "http://localhost:8081",
            5
        );
    }
}
