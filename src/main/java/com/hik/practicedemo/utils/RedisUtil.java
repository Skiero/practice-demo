package com.hik.practicedemo.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by wangJinChang on 2019/11/21 19:04
 * redis工具类
 */
@Slf4j
public class RedisUtil {

    private static final String DEFAULT_REDIS_BEAN_NAME = "redisTemplate";

    private static RedisTemplate<String, String> redisTemplate;

    private static RedisTemplate<String, String> getRedisTemplate() {
        if (redisTemplate == null) {
            redisTemplate = (RedisTemplate<String, String>) SpringBeanContextUtil.getBean(DEFAULT_REDIS_BEAN_NAME);
        }
        return redisTemplate;
    }

    /**
     * 设置redis缓存
     *
     * @param key   redisKey
     * @param value redisValue
     */
    public static void set(String key, String value) {
        getRedisTemplate().opsForValue().set(key, value == null ? "" : value);
        log.debug("RedisInfo: add key = {} successfully! ", key);
    }

    /**
     * 设置redis缓存
     *
     * @param key     redisKey
     * @param value   redisValue
     * @param timeout 过期时间(秒)
     */
    public static void set(String key, String value, long timeout) {
        getRedisTemplate().opsForValue().set(key, value == null ? "" : value, timeout, TimeUnit.SECONDS);
        log.debug("RedisInfo: add key = {} successfully!, expire time = {} seconds", key, timeout);
    }

    /**
     * 设置redis缓存
     *
     * @param key     redisKey
     * @param value   redisValue
     * @param timeout 过期时间
     * @param unit    过期时间单位
     */
    public static void set(String key, String value, long timeout, TimeUnit unit) {
        getRedisTemplate().opsForValue().set(key, value == null ? "" : value, timeout, unit);
        log.debug("RedisInfo: add key = {} , successfully!, expire time = {} seconds", key, timeout);
    }

    /**
     * 根据redisKey获取redis缓存
     *
     * @param key redisKey
     * @return redisValue
     */
    public static String get(String key) {
        String value = getRedisTemplate().opsForValue().get(key);
        log.debug("RedisInfo: get value by key = {} , value = {}", key, value);
        return value == null ? "" : value;
    }

    /**
     * 根据redisKey删除redis缓存
     *
     * @param key redisKey
     */
    public static void delete(String key) {
        getRedisTemplate().opsForValue().getOperations().delete(key);
        log.debug("RedisInfo: delete key = {} successfully! ", key);
    }

    /**
     * 根据redisKey设置redis缓存获取时间
     *
     * @param key     redisKey
     * @param timeout 过期时间(秒)
     */
    public static void setExpire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
        log.debug("RedisInfo: setExpire = {} by key = {} successfully! ", timeout, key);
    }

    /**
     * 根据redisKey获取redis缓存过期时间
     *
     * @param key redisKey
     * @return Long(秒)
     */
    public static Long getExpire(String key) {
        log.debug("RedisInfo: getExpire by key = {} successfully! ", key);
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key redisKey
     * @return 存在:true   不存在:false
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
