package com.hik.practicedemo.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by wangJinChang on 2019/11/20 16:55
 * SpringBean上下文工具类
 */
@Component
public class SpringBeanContextUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private static ApplicationContext getSpringContext() {
        return applicationContext;
    }

    /**
     * 根据bean名称获取bean
     *
     * @param name bean名称
     * @return 实例
     */
    public static Object getBean(String name) {
        return getSpringContext().getBean(name);
    }

    /**
     * 根据bean class获取bean
     *
     * @param clazz bean class
     * @param <T>   泛型
     * @return 实例
     */
    public static <T> T getBean(Class<T> clazz) {
        return getSpringContext().getBean(clazz);
    }

    /**
     * 根据bean名称和bean class获取bean
     *
     * @param name  bean名称
     * @param clazz bean class
     * @param <T>   泛型
     * @return 实例
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getSpringContext().getBean(name, clazz);
    }
}
