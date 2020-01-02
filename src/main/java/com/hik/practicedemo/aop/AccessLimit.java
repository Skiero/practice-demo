package com.hik.practicedemo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangJinChang on 2019/12/28 11:08
 * 访问限流注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    int maxCount() default 600;                         // 最大请求量，默认600

    int seconds() default 60;                           // 时间，默认60

    TimeUnit timeUnit() default TimeUnit.SECONDS;       // 时间单位，默认S

    boolean needLogin() default true;                   // 是否需要登录，默认是
}
