package com.platform.testing.dto.task;

import com.platform.testing.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: QueryTestTaskListDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-22 20:42
 **/
@ApiModel(value="查询任务信息列表对象")
@Data
public class QueryTestTaskListDto extends BaseDto {

    @ApiModelProperty(value="任务名称")
    private String name;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;

}
