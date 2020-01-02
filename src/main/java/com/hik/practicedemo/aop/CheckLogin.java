package com.hik.practicedemo.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangJinChang on 2019/12/30 11:24
 * 验证是否登录的注解  ==>  使用AOP环绕通知实现
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLogin {

    @AliasFor("fileName")
    String value() default "id";

    @AliasFor("value")
    String fileName();
}
