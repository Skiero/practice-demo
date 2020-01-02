package com.hik.practicedemo.conf.verification.impl;

import com.hik.practicedemo.conf.verification.annotation.Phone;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Created by wangJinChang on 2019/12/26 10:35
 * 手机号码校验注解实现类
 */
public class PhoneValidatorImpl implements ConstraintValidator<Phone, Object> {

    private String regexp;

    private boolean notEmpty;

    @Override
    public void initialize(Phone constraintAnnotation) {
        regexp = constraintAnnotation.regexp();
        notEmpty = constraintAnnotation.notEmpty();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return !notEmpty;
        }
        return Pattern.matches(regexp, (CharSequence) value);
    }
}
