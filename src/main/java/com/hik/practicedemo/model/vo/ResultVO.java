package com.hik.practicedemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by wangJinChang on 2019/11/4 19:18
 * 结果视图对象  0表示成功，1表示失败，2表示其他
 */
@ApiModel
public class ResultVO<T> implements Serializable {

    private static final long serialVersionUID = -8750517585513764607L;

    private static final String SUCCESS_CODE = "0";

    private static final String SUCCESS_MSG = "success";

    private static final String FAILURE_CODE = "1";

    private static final String OTHER_CODE = "2";

    @ApiModelProperty(value = "状态", dataType = "String", example = "0", position = 0)
    private String code;

    @ApiModelProperty(value = "信息", dataType = "String", example = "success", position = 1)
    private String msg;

    @ApiModelProperty(value = "数据", position = 2)
    private T data;

    private ResultVO(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ResultVO(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResultVO success() {
        return new ResultVO(SUCCESS_CODE, SUCCESS_MSG);
    }

    public static <T> ResultVO<T> success(T data) {
        return new ResultVO<>(SUCCESS_CODE, SUCCESS_MSG, data);
    }

    public static <T> ResultVO<T> error(String msg) {
        return new ResultVO<>(FAILURE_CODE, msg);
    }

    public static <T> ResultVO<T> error(String code, String msg) {
        return new ResultVO<>(code, msg);
    }

    public static <T> ResultVO<T> other(String msg) {
        return new ResultVO<>(OTHER_CODE, msg);
    }
}