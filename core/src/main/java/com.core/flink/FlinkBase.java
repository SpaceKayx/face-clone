package com.core.flink;

import lombok.NonNull;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public abstract class FlinkBase {
    protected StreamExecutionEnvironment env;

    protected abstract @NonNull String getJobName();

    protected abstract void process(@NonNull String jobName) throws Exception;

//    protected abstract @NonNull StreamExecutionEnvironment initEnv(@NonNull String jobName);

    private Configuration setupEnvironmentFlink() {
        Configuration config = new Configuration();
        config.setBoolean("rest.enable-web-submission", true); // active web ui
        config.setInteger(RestOptions.PORT, 8082);              // setup port
        config.setString(RestOptions.ADDRESS, "localhost");
        config.setString(JobManagerOptions.ADDRESS, "localhost"); // Địa chỉ local mode
        return config;
    }


    protected void run(String[] args) throws Exception {

        String jobName = getJobName();
        env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(setupEnvironmentFlink());
//        env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        process(jobName);

        env.execute(jobName).getJobExecutionResult();
    }

}
