package com.hik.practicedemo.exception;

/**
 * Created by wangJinChang on 2019/12/26 20:24
 * 通用异常枚举
 */
public enum CommonExceptionEnum implements CommonException {

    OTHER_ERROR("10001", "Other error"),

    RESOURCE_NOT_EXIST("10002", "Resource does not exist"),

    OPERATION_FAILED("10003", "Operation failed"),

    OTP_EXPIRED("10004", "Otp has expired"),

    OTP_VERIFIED_FAILED("10005", "Otp verified failed"),

    USER_NOT_LOGIN("10006", "User not login"),

    TOO_MANY_REQUESTS("10007", "Too many requests"),


    ;

    private String code;

    private String message;

    CommonExceptionEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.message;
    }

    @Override
    public CommonException setMsg(String message) {
        this.message = message;
        return this;
    }
}
