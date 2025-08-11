package com.core.kafka;

import com.core.kafka.message.BaseMessage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseKafkaHandler<T> {

    protected abstract void processMessage(T message);

    protected void handleMessage(String rawMessage, String topic, Class<T> clazz) {
        log.info("Start handleMessage by Kafka Handler");
        if (rawMessage == null) {
            log.warn("rawMessage in topic {} is null", topic);
            return;
        }

        try {
            T message = new BaseMessage().getValue(clazz, rawMessage);
            if (message == null) {
                log.error("Cannot parse message from topic {} to {}", topic, clazz.getSimpleName());
                return;
            }

            processMessage(message);

            log.info("Successfully processed message from topic {}", topic);
            log.info("End handleMessage by Kafka Handler");
        } catch (Exception e) {
            log.error("Error processing message from topic {}: {}", topic, e.getMessage(), e);
        }
    }

}
