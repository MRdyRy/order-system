package com.rudy.ryanto.order.system.kafka.producer;

import com.rudy.ryanto.order.system.kafka.config.data.KafkaConfigData;
import com.rudy.ryanto.order.system.kafka.config.data.KafkaProducerConfigData;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@SpringBootConfiguration
public class KafkaProducerConfig <K extends Serializable, V extends SpecificRecordBase>{

    private final KafkaConfigData kafkaConfigData;
    private final KafkaProducerConfigData kafkaProducerConfigData;

    public KafkaProducerConfig(KafkaConfigData kafkaConfigData, KafkaProducerConfigData kafkaProducerConfigData) {
        this.kafkaConfigData = kafkaConfigData;
        this.kafkaProducerConfigData = kafkaProducerConfigData;
    }

    @Bean
    public Map<String, Object> producerConfig(){
        Map<String,Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaConfigData.getBootstrapServers());
        properties.put(kafkaConfigData.getSchemaRegistryUrl(),kafkaConfigData.getSchemaRegistryUrl());
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,kafkaProducerConfigData.getKeySerializerClass());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,kafkaProducerConfigData.getValueSerializerClass());
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, kafkaProducerConfigData.getBatchSize() * kafkaProducerConfigData.getBatchSizeBoostFactor());
        properties.put(ProducerConfig.LINGER_MS_CONFIG, kafkaProducerConfigData.getLingerMs());
        properties.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, kafkaProducerConfigData.getCompressionType());
        properties.put(ProducerConfig.ACKS_CONFIG, kafkaProducerConfigData.getAcks());
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, kafkaProducerConfigData.getRequestTimeoutMs());
        properties.put(ProducerConfig.RETRIES_CONFIG, kafkaProducerConfigData.getRetryCount());

        return properties;
    }

    @Bean
    public ProducerFactory<K,V> producerFactory(){
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public KafkaTemplate<K,V> kafkaTemplate(){
        return new KafkaTemplate<>(producerFactory());
    }
}
