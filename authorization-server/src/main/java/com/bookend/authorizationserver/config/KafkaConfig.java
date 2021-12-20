package com.bookend.authorizationserver.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${kafka.endpoint}")
    private String kafkaEndpoint;

    @Value("${kafka.port}")
    private String kafkaPort;


    @Bean
    public KafkaTemplate<Integer, String> createTemplate(KafkaProperties properties)
    {
        List<String> x = new ArrayList<>();
        x.add(kafkaEndpoint+":"+kafkaPort);
        properties.setBootstrapServers(x);
        Map<String, Object> props = properties.buildProducerProperties();

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        KafkaTemplate<Integer, String> template = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
        return template;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory()
    {
        Map<String,Object> config = new HashMap<>();

        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaEndpoint+":"+kafkaPort);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return  new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate kafkaTemplate()
    {
        return  new KafkaTemplate<>(producerFactory());
    }
}
