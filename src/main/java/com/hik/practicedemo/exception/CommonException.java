package com.hik.practicedemo.exception;

/**
 * Created by wangJinChang on 2019/12/26 20:21
 * 通用异常接口
 */
public interface CommonException {

    String getCode();

    String getMsg();

    CommonException setMsg(String message);
}
