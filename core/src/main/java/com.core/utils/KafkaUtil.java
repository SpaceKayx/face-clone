package com.core.utils;

import com.core.kafka.message.BaseMessage;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.flink.connector.kafka.sink.KafkaSink;

import java.nio.charset.StandardCharsets;

public class KafkaUtil {
    public static KafkaSink<BaseMessage> buildKafkaSink(String bootstrapServer) {
        return KafkaSink.<BaseMessage>builder()
                .setBootstrapServers(bootstrapServer)
                .setRecordSerializer(KafkaRecordSerializationSchema.<BaseMessage>builder()
                        .setTopicSelector(BaseMessage::getTopic)
                        .setValueSerializationSchema(msg -> JSONUtil.toJson(msg).getBytes(StandardCharsets.UTF_8))
                        .setKeySerializationSchema(msg -> msg.getKey().getBytes(StandardCharsets.UTF_8))
                        .build()
                )
                .build();
    }
}
