package com.core.kafka.producer;

import com.core.kafka.message.BaseMessage;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class BaseProducerHandler extends BaseProducer<BaseMessage> {
    public BaseProducerHandler(KafkaTemplate<String, BaseMessage> kafkaTemplate) {
        super(kafkaTemplate);
    }
}
