package com.hik.practicedemo.model.dto;

import lombok.Data;

/**
 * Created by wangJinChang on 2019/12/26 12:02
 * 用户信息传输对象
 */
@Data
public class UserInfoDTO {

    private Integer id;

    private String name;

    private String tel;

    private String email;

    private String pwd;
}
