package com.hik.practicedemo.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangJinChang on 2019/11/23 15:45
 * HTTP工具类
 */
public class HttpUtil {

    public static HttpServletRequest getRequest() {
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        if (ra == null) {
            return null;
        }
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return request;
    }

    public static Map<String, String> getTokenAsMap() {
        Map<String, String> token = new HashMap<>(2);
        token.put("token", "test");
        return token;
    }

    public static HttpEntity<String> getHttpEntity(String jsonText) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("token", "test");
        return new HttpEntity<>(jsonText, headers);
    }

    public static <T> HttpEntity<T> getHttpEntity(T obj) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.add("token", "test");
        return new HttpEntity<>(obj, headers);
    }

    public static void write(String str, HttpServletResponse response) {
        if (response == null) {
            throw new NullPointerException("HttpServletResponse should not be null!");
        } else {
            PrintWriter writer = null;
            try {
                response.setContentType("text/html; charset=utf-8");
                writer = response.getWriter();
                writer.write(str);
            } catch (IOException var7) {
                var7.printStackTrace();
            } finally {
                if (writer != null) {
                    writer.close();
                }
            }
        }
    }
}
