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
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.triggers.CountTrigger;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;

import java.io.Serializable;
import java.util.Properties;
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

        FlinkKafkaConsumer<String> kafkaSource = new FlinkKafkaConsumer<>(
                FConstants.TOPIC_USER_FORGET_PW,
                new SimpleStringSchema(),
                createKafkaProperties()
        );

        env
                .addSource(kafkaSource)
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

    private Properties createKafkaProperties() {
        Properties kafkaProperties = new Properties();
        kafkaProperties.setProperty("bootstrap.servers", "localhost:9092");
        kafkaProperties.setProperty("group.id", FConstants.GROUP_ID_DEFAULT);
        kafkaProperties.setProperty("max.poll.interval.ms", "600000"); // tang thoi gian kafka xu ly
        kafkaProperties.setProperty("max.poll.records", "100"); // giam so luong ban ghi xu ly 1 lan
        kafkaProperties.setProperty("enable.auto.commit", "false"); // tắt auto commit và để Flink tự commit offset

        return kafkaProperties;
    }
}
