package com.hik.practicedemo.conf.async;

import com.hik.practicedemo.model.enums.AsyncTaskEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.*;

/**
 * Created by wangJinChang on 2019/12/26 19:20
 * 自定义线程池
 */
@Configuration
@EnableAsync
@EnableScheduling
public class AsyncConfig {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int MAXIMUM_POOL_SIZE = CORE_POOL_SIZE * 2;

    private static final int KEEP_ALIVE_TIME = 60 * 60;

    @Bean(name = "asyncExecutorThreadPool")
    public Executor loadAsyncExecutor() {

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setQueueCapacity(100);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setThreadNamePrefix(AsyncTaskEnum.ASYNC_EXECUTOR.getThreadNamePrefix());
        return executor;
    }

    @Bean(name = "asyncSchedulerThreadPool")
    public Executor loadAsyncScheduler() {

        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(CORE_POOL_SIZE);
        scheduler.setThreadNamePrefix(AsyncTaskEnum.ASYNC_SCHEDULER.getThreadNamePrefix());
        return scheduler;
    }
}
