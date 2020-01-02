package com.hik.practicedemo.model.enums;

/**
 * Created by wangJinChang on 2019/12/26 19:39
 * 异步任务枚举
 */
public enum AsyncTaskEnum {

    ASYNC_EXECUTOR("asyncExecutorThreadPool", "async-executor-"),

    ASYNC_SCHEDULER("asyncSchedulerThreadPool", "async-scheduler-"),

    ;

    private String beanName;

    private String threadNamePrefix;

    AsyncTaskEnum(String beanName, String threadNamePrefix) {
        this.beanName = beanName;
        this.threadNamePrefix = threadNamePrefix;
    }

    public String getBeanName() {
        return beanName;
    }

    public String getThreadNamePrefix() {
        return threadNamePrefix;
    }
}
