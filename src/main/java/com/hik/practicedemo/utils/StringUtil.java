package com.hik.practicedemo.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangJinChang on 2019/12/26 21:02
 * 字符串工具类
 */
public class StringUtil {

    /**
     * 加密手机号码
     *
     * @param phoneNumber 手机号码明文
     * @return 加密中间四位后的手机号码
     */
    public static String encryptPhoneNumber(String phoneNumber) {
        if (StringUtils.isEmpty(phoneNumber)) {
            return phoneNumber;
        }
        return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 加密邮箱
     *
     * @param email 邮箱明文
     * @return 加密中间四位后的邮箱
     */
    public static String encryptEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return email;
        }
        StringBuilder sb = new StringBuilder(email);
        return sb.replace(2, sb.lastIndexOf("@") - 2, "****").toString();
    }
}
