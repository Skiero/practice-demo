package com.hik.practicedemo.conf.verification.annotation;

import com.hik.practicedemo.conf.verification.impl.PhoneValidatorImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Created by wangJinChang on 2019/12/26 10:23
 * 手机号码校验注解
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidatorImpl.class)
public @interface Phone {

    String message() default "{javax.validation.constraints.Phone.message}";

    boolean notEmpty() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String regexp() default ".*";
}
