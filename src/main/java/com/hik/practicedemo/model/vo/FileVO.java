package com.hik.practicedemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by wangJinChang on 2019/12/27 20:20
 * 文件视图模型
 */
@Data
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
public class FileVO implements Serializable {

    private static final long serialVersionUID = -2164500982564287821L;

    @ApiModelProperty(name = "fileId", notes = "文件id", position = 0)
    private String fileId;

    @ApiModelProperty(name = "fileName", notes = "文件真实名称", position = 1)
    private String fileName;

    @ApiModelProperty(name = "filePath", notes = "文件访问路径", position = 2)
    private String filePath;
}
