package com.core.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaConfigProperties {

    private String bootstrapServers;

    private ConsumerProperties consumer = new ConsumerProperties();

    @Getter
    @Setter
    public static class ConsumerProperties {
        private String groupId;
    }

}