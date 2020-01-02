package com.hik.practicedemo.model.param.user;

import com.hik.practicedemo.conf.verification.annotation.Phone;
import com.hik.practicedemo.conf.verification.sequence.First;
import com.hik.practicedemo.conf.verification.sequence.Second;
import com.hik.practicedemo.conf.verification.sequence.Third;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.GroupSequence;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by wangJinChang on 2019/12/23 21:03
 * 用户注册时的请求参数
 */
@Data
@ApiModel
@GroupSequence({First.class, Second.class, Third.class, UserRegisterParam.class})
public class UserRegisterParam {

    @ApiModelProperty(value = "验证码", dataType = "String", example = "1234", position = 0)
    @NotEmpty(message = "验证码不能为空")
    private String otp;

    @ApiModelProperty(value = "手机号码", dataType = "String", example = "18390220023", position = 1)
    @NotEmpty(message = "手机号码不能为空", groups = First.class)
    @Phone(message = "手机号码格式不正确", regexp = "^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\\d{8}$", groups = Second.class)
    private String tel;

    @ApiModelProperty(value = "用户邮箱", dataType = "String", example = "18390220023@126.com", position = 2)
    @NotEmpty(message = "用户邮箱不能为空", groups = First.class)
    @Email(message = "邮箱格式不正确", regexp = "[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?", groups = Second.class)
    private String email;

    @ApiModelProperty(value = "用户密码", dataType = "String", example = "xf12345+", position = 3)
    @NotEmpty(message = "用户密码不能为空")
    private String pwd;
}
