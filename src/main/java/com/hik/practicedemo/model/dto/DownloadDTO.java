package com.hik.practicedemo.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Created by wangJinChang on 2019/12/26 12:12
 * 下载数据传输对象
 */
@Data
@Accessors(chain = true)
public class DownloadDTO {

    private byte[] bytes;   // 二进制码

    private String name;    // 文件原名

    private String type;    // 文件类型
}
