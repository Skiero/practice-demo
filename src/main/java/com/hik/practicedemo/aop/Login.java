package com.hik.practicedemo.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by wangJinChang on 2019/12/28 16:07
 * 验证是否登录的注解  ==>  使用拦截器实现
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Login {

    @AliasFor("attributeKey")
    String value() default "";              // request域对象的key

    @AliasFor("value")
    String attributeKey() default "";       // request域对象的key
}
