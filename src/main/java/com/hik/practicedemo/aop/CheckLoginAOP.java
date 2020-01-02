package com.hik.practicedemo.aop;

import com.hik.practicedemo.exception.CommonExceptionEnum;
import com.hik.practicedemo.model.vo.ResultVO;
import com.hik.practicedemo.utils.CasUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/12/30 11:25
 * 验证是否登录切面类
 */
@Component
@Aspect
public class CheckLoginAOP {

    @Pointcut("@annotation(com.hik.practicedemo.aop.CheckLogin)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();   // 获取切点对应的方法
        CheckLogin checkLogin = methodSignature.getMethod().getAnnotation(CheckLogin.class);    // 获取该方法对应的注解
        String fieldName = checkLogin.fileName();
        Class clazz = joinPoint.getSignature().getDeclaringType();
        Field field = clazz.getDeclaredField(fieldName);    // 通过反射获取私有的属性
        field.setAccessible(true);  // 暴力破解
        Optional<Integer> optional = CasUtil.getUserId();
        if (optional.isPresent()) {
            field.set(joinPoint.getTarget(), optional.get());   // 对该属性赋值
        } else {
            return ResultVO.error(CommonExceptionEnum.USER_NOT_LOGIN.getCode(), CommonExceptionEnum.USER_NOT_LOGIN.getMsg());
        }
        return joinPoint.proceed();
    }
}
