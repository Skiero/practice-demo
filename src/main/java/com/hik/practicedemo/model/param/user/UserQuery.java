package com.hik.practicedemo.model.param.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import java.util.Date;
import java.util.List;

/**
 * Created by wangJinChang on 2019/12/27 15:09
 * 模糊查询用户信息  --  查询条件
 */
@Data
@ApiModel
public class UserQuery {

    @ApiModelProperty(value = "用户id集合", dataType = "List", example = "1", position = 0)
    private List<Integer> ids;

    @ApiModelProperty(value = "用户名", dataType = "String", example = "tom", position = 1)
    private String name;

    @ApiModelProperty(value = "用户手机", dataType = "String", example = "13100230023", position = 2)
    private String tel;

    @ApiModelProperty(value = "注册开始时间", dataType = "Date", example = "", position = 3)
    private Date registerStartTime;

    @ApiModelProperty(value = "注册结束时间", dataType = "Date", example = "", position = 4)
    private Date registerEndTime;

    @ApiModelProperty(value = "当前页码", dataType = "Integer", example = "1", position = 5)
    @Min(value = 1, message = "当前页面必须大于0")
    private Integer pageNo;

    @ApiModelProperty(value = "页面大小", dataType = "Integer", example = "20", position = 6)
    @Range(min = 1, max = 100, message = "页面内容必须介于1~100")
    private Integer pageSize;
}
