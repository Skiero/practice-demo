package com.hik.practicedemo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by wangJinChang on 2019/12/27 19:50
 * 文件实体类
 */
@Entity
@Table(name = "tb_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true, length = 64)
    private String id;                  //文件id

    @Column(name = "bucket_name", nullable = false, length = 64)
    private String bucketName;          //桶名

    @Column(name = "original_file_name", nullable = false, length = 64)
    private String originalFileName;    //文件原名

    @Column(name = "file_type", nullable = false, length = 64)
    private String fileType;            //文件格式

    @Column(name = "minion_file_name", nullable = false, length = 64)
    private String minionFileName;      //保存时的名称

    @Column(name = "user_id", nullable = false, length = 12)
    private String userId;              //操作员

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    private Date createTime;            //创建时间

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "update_time")
    private Date updateTime;            //更新时间
}
