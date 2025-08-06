package com.userservice.flink;

import com.core.constants.FConstants;
import com.core.flink.FlinkBase;
import com.core.kafka.message.BaseMessage;
import com.core.utils.KafkaUtil;
import com.userservice.flink.dtos.ForgetPasswordDTO;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.stream.StreamSupport;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HandleForgetPassword extends FlinkBase implements Serializable {

    public static void main(String[] args) throws Exception {
        new HandleForgetPassword().run(args);
    }

    @Override
    protected @NonNull String getJobName() {
        return String.format("%s-%s", getClass().getCanonicalName(), System.currentTimeMillis());
    }

    @Override
    protected void process(@NonNull String jobName) throws Exception {

        KafkaSource<String> kafkaSource = KafkaSource.<String>builder()
                .setBootstrapServers("localhost:9092")
                .setTopics(FConstants.TOPIC_USER_FORGET_PW)
                .setGroupId(FConstants.GROUP_ID_DEFAULT)
                .setValueOnlyDeserializer(new SimpleStringSchema())
                .setStartingOffsets(OffsetsInitializer.latest())
                .build();

        env
                .fromSource(kafkaSource, WatermarkStrategy.noWatermarks(), jobName)
                .map(message -> new BaseMessage().getValue(ForgetPasswordDTO.class, message))
                .keyBy(ForgetPasswordDTO::getEmail)
                .countWindow(5, 1)
                .trigger(CountTrigger.of(5))
                .process(new ProcessWindowFunction<ForgetPasswordDTO, BaseMessage, String, GlobalWindow>() {
                    @Override
                    public void process(String email, Context context, Iterable<ForgetPasswordDTO> elements, Collector<BaseMessage> out) {
                        long count = StreamSupport.stream(elements.spliterator(), false).count();
                        if (count >= 5) {
                            ForgetPasswordDTO latest = StreamSupport
                                    .stream(elements.spliterator(), false)
                                    .reduce((first, second) -> second)
                                    .orElse(null);

                            if (latest != null) {
                                out.collect(BaseMessage.builder()
                                        .key(email)
                                        .value(latest)
                                        .topic(FConstants.TOPIC_USER_LOCKED)
                                        .build());
                            }
                        }
                    }
                })
                .sinkTo(KafkaUtil.buildKafkaSink("localhost:9092"));
    }
}
