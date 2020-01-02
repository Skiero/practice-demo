package com.hik.practicedemo.conf.async;

import com.hik.practicedemo.utils.SpringBeanContextUtil;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Created by wangJinChang on 2019/12/26 21:42
 * 异步任务测试类
 */
@Component
public class TestAsync {

    @Async(value = "asyncExecutorThreadPool")
    public void testExecutorThreadPool() {
        for (int i = 0; i < 1; i++) {
            System.out.println(Thread.currentThread().getName() + "  任务线程池  ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    @Async(value = "asyncSchedulerThreadPool")
    public void testSchedulerThreadPool() {
        for (int i = 0; i < 1; i++) {
            System.out.println(Thread.currentThread().getName() + "  定时线程池  ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

//    @Scheduled(cron = "0/600 * * * * ?")  // 60*10s执行一次
    @Async("asyncSchedulerThreadPool")
    public void testScheduledByAssign() {
        for (int i = 0; i < 1; i++) {
            System.out.println(Thread.currentThread().getName() + "  定时任务+定时线程池  ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

//    @Scheduled(cron = "0/600 * * * * ?")  // 60*10s执行一次
    public void testScheduled() {
        for (int i = 0; i < 1; i++) {
            System.out.println(Thread.currentThread().getName() + "  定时任务  ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void testExecutor() {
        Executor executor = (Executor) SpringBeanContextUtil.getBean("asyncExecutorThreadPool");
        CompletableFuture.runAsync(() -> {
                    for (int i = 0; i < 1; i++) {
                        System.out.println(Thread.currentThread().getName() + "  这是执行器多线程测试  ");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                , executor);
        this.testExecutorThreadPool();

        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 1; i++) {
                System.out.println(Thread.currentThread().getName() + "  这是系统自带多线程测试  ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        this.testSchedulerThreadPool();
    }
}
