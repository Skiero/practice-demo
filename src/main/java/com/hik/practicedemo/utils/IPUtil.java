package com.hik.practicedemo.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangJinChang on 2019/12/28 11:19
 * ip工具类
 */
public class IPUtil {

    /**
     * 获得当前访问的IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    public static String getIP(HttpServletRequest request) {
        String ip = request.getHeader("X-real-ip");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1") || StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    /**
     * 获得当前访问的IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    public static String getResolvedHostIp(HttpServletRequest request) {
        String Ip;
        try {
            String host = request.getHeader("Host");
            String name = StringUtils.EMPTY;
            if (StringUtils.isNotEmpty(host)) {
                if (host.contains(":")) {
                    int colonPosition = host.indexOf(":");
                    name = host.substring(0, colonPosition);
                } else {
                    name = host;
                }
            }
            Ip = InetAddress.getByName(name).getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Ip = getHostIP(request);
        }
        return Ip;
    }

    /**
     * 获得当前访问的IP地址
     *
     * @param request HttpServletRequest
     * @return IP地址
     */
    public static String getHostIP(HttpServletRequest request) {
        String ip = "";
        String host = request.getHeader("Host");
        if (StringUtils.isNotEmpty(host)) {
            if (host.contains(":")) {
                int colonPosition = host.indexOf(":");
                ip = host.substring(0, colonPosition);
            } else {
                ip = host;
            }
        } else {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 获得当前访问的IP地址
     *
     * @return IP地址
     */
    public static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";
        StringBuilder inputLine = new StringBuilder();
        String read;
        URL url;
        HttpURLConnection urlConnection;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            while ((read = in.readLine()) != null) {
                inputLine.append(read).append("\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            ip = m.group(1);
        }
        return ip;
    }
}
