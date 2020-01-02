package com.hik.practicedemo.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangJinChang on 2019/12/4 14:46
 * 用户实体类
 */
@Entity
@Table(name = "tb_user")
@Data
@Accessors(chain = true)
public class UserEntity {
    @Id
    @Column(name = "id", unique = true, length = 20)
    private Integer id;

    @Column(name = "name", unique = true, length = 64)
    private String name;

    @Column(name = "tel", unique = true, length = 11, nullable = false)
    private String tel;

    @Column(name = "email", unique = true, length = 64, nullable = false)
    private String email;

    @Column(name = "pwd", length = 64, nullable = false)
    private String pwd;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time", nullable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time", nullable = false)
    private Date updateTime;
}
