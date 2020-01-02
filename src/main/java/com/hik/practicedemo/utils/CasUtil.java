package com.hik.practicedemo.utils;

import com.alibaba.fastjson.JSON;
import com.hik.practicedemo.model.constant.CommonConstants;
import com.hik.practicedemo.model.vo.UserVO;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by wangJinChang on 2019/11/23 15:42
 * CAS登录工具类
 */
public class CasUtil {
    /**
     * 获取登录用户的用户信息
     *
     * @param request HttpServletRequest
     * @return 登录的用户信息
     */
    private static UserVO getUserInfo(HttpServletRequest request) {
        request = request == null ? HttpUtil.getRequest() : null;
        if (request != null) {
            Cookie[] cookies = request.getCookies();
            if (Objects.isNull(cookies)) {
                return null;
            }
            for (Cookie cookie : cookies) {
                if (Objects.equals(CommonConstants.LOGIN_TOKEN, cookie.getName())) {
                    //使用 String.format 方法后会将 cookie.getValue() 转化为大写，进而无法取出 redisValue
                    //String redisKey = String.format(CommonConstants.LOGIN_TOKEN + "%S", cookie.getValue());
                    String s = RedisUtil.get(CommonConstants.LOGIN_TOKEN + cookie.getValue());
                    return JSON.parseObject(s, UserVO.class);
                }
            }
        }
        return null;
    }

    /**
     * 获取登录用户的用户名
     *
     * @return 登录用户的用户名
     */
    public static Optional<String> getUserName() {
        UserVO userVO = getUserInfo(null);
        if (Objects.isNull(userVO)) {
            return Optional.empty();
        }
        return Optional.ofNullable(userVO.getName());
    }

    /**
     * 获取登录用户的ID
     *
     * @return 登录用户的ID
     */
    public static Optional<Integer> getUserId() {
        UserVO userVO = getUserInfo(null);
        if (Objects.isNull(userVO)) {
            return Optional.empty();
        }
        return Optional.ofNullable(userVO.getId());
    }

    /**
     * 多线程中获取登录用户的ID
     *
     * @param request HttpServletRequest
     * @return 登录用户的ID
     */
    public static Optional<Integer> getUserId(HttpServletRequest request) {
        UserVO userVO = getUserInfo(request);
        if (Objects.isNull(userVO)) {
            return Optional.empty();
        }
        return Optional.ofNullable(userVO.getId());
    }

    /**
     * 获取登录用户的token(cookie的value)
     *
     * @return token
     */
    public static Optional<String> getUserToken() {
        HttpServletRequest request = HttpUtil.getRequest();
        if (request == null) {
            return Optional.empty();
        }
        Cookie[] cookies = request.getCookies();
        if (Objects.isNull(cookies)) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (Objects.equals(CommonConstants.LOGIN_TOKEN, cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
