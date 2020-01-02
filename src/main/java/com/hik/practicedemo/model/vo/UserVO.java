package com.hik.practicedemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by wangJinChang on 2019/12/26 11:59
 * 用户视图对象
 */
@Data
@ApiModel
public class UserVO implements Serializable {

    private static final long serialVersionUID = 3078797970910351173L;

    @ApiModelProperty(value = "用户id", dataType = "Integer", example = "1001", position = 0)
    private Integer id;

    @ApiModelProperty(value = "用户名", dataType = "String", example = "", position = 1)
    private String name;

    @ApiModelProperty(value = "用户手机", dataType = "String", example = "", position = 2)
    private String tel;

    @ApiModelProperty(value = "用户邮箱", dataType = "String", example = "", position = 3)
    private String email;
}
