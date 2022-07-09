package com.platform.testing.dto.testcase;

import com.platform.testing.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: UpdateTestTaskStatusDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-22 16:23
 **/
@ApiModel(value="修改任务状态对象")
@Data
public class UpdateTestTaskStatusDto extends BaseEntityNew {

    /**
     * ID
     */
    @ApiModelProperty(value="任务主键",required=true)
    private Integer taskId;

    /**
     *
     */
    @ApiModelProperty(value="构建地址",required=true)
    private String buildUrl;

    /**
     *
     */
    @ApiModelProperty(value="任务状态码",required=true)
    private Integer status;

}