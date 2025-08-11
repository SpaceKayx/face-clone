package com.core.kafka.producer;


import com.core.kafka.message.BaseMessage;
import com.core.utils.JSONUtil;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class BaseProducer<T extends BaseMessage> {
    private final KafkaTemplate<String, T> kafkaTemplate;

    public void send(T event) {
        log.info("Start send event with topic={} and value={}", event.getTopic(), event.getValue());
        ProducerRecord<String, T> record = createRecord(event);
        kafkaTemplate.send(record).whenComplete(this::handleCompletion);
    }

    private ProducerRecord createRecord(T event) {
        List<Header> headers = this.createHeader(event.getHeaders());
        return new ProducerRecord(event.getTopic(), event.getPartition(), event.getKey(), JSONUtil.toJson(event), headers);
    }

    private List<Header> createHeader(Map<String, String> headers) {
        if (Objects.isNull(headers)) {
            return Collections.emptyList();
        }
        return headers.entrySet().stream().map(header ->
                        new RecordHeader(header.getKey(), header.getValue().getBytes()))
                .collect(Collectors.toList());
    }

    private void handleCompletion(SendResult<String, T> stringTSendResult, Throwable throwable) {
        if (throwable != null) {
            handleSendFailure(throwable);
        } else {
            handleSendSuccess(stringTSendResult);
        }
    }

    private void handleSendSuccess(SendResult<String, T> result) {
        log.info("Send message success with result {}", result);
    }

    private void handleSendFailure(Throwable throwable) {
        log.info("Send message failure with result {}", throwable.getMessage());
    }

    @PreDestroy
    protected void close() {
        if (kafkaTemplate != null) {
            kafkaTemplate.destroy();
        }
    }

}
