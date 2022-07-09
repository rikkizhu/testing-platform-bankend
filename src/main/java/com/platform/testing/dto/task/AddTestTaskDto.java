package com.platform.testing.dto.task;

import com.platform.testing.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: AddTestTaskDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-21 21:06
 **/
@ApiModel(value = "添加任务对象")
@Data
public class AddTestTaskDto extends BaseEntityNew {
    /**
     * 名称
     */
    @ApiModelProperty(value = "任务名称", required = true)
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value = "任务备注")
    private String remark;

    /**
     * 运行测试的Jenkins服务器id
     */
    @ApiModelProperty(value = "运行测试的Jenkins服务器id", required = true)
    private Integer testJenkinsId;


    @ApiModelProperty(value = "创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
