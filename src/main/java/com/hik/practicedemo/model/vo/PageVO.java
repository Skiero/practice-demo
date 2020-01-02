package com.hik.practicedemo.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by wangJinChang on 2019/12/27 15:04
 * 分页视图模型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class PageVO<T> {

    @ApiModelProperty(value = "总页数", dataType = "Integer", example = "10", position = 0)
    private Integer totalPage;  //总页数

    @ApiModelProperty(value = "记录总数", dataType = "Long", example = "100", position = 1)
    private Long total;         //记录总数

    @ApiModelProperty(value = "当前页码", dataType = "Integer", example = "1", position = 2)
    private Integer pageNo;     //当前页码

    @ApiModelProperty(value = "分页大小", dataType = "Integer", example = "10", position = 3)
    private Integer pageSize;   //分页大小

    @ApiModelProperty(value = "返回数据", dataType = "List", example = "", position = 4)
    private List<T> list;       //返回数据
}
