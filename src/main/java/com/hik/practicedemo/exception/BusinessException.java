package com.hik.practicedemo.exception;

/**
 * Created by wangJinChang on 2019/12/26 20:33
 * 业务异常类
 */
public class BusinessException extends Exception implements CommonException {

    private static final long serialVersionUID = 3469114512032313233L;

    private CommonException commonException;

    public BusinessException(CommonException commonException) {
        super();
        this.commonException = commonException;
    }

    public BusinessException(CommonException commonException, String message) {
        super();
        this.commonException = commonException;
        this.commonException.setMsg(message);
    }

    @Override
    public String getCode() {
        return this.commonException.getCode();
    }

    @Override
    public String getMsg() {
        return this.commonException.getMsg();
    }

    @Override
    public CommonException setMsg(String message) {
        this.commonException.setMsg(message);
        return this;
    }
}
