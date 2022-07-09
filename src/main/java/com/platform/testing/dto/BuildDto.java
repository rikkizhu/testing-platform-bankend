package com.platform.testing.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: UserDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-15 17:14
 **/
@ApiModel(value = "jenkins构建",description = "jenkins构建")
@Data
public class BuildDto {

    @ApiModelProperty(value = "job名称",example = "rikky",required =true)
    private String jobName;


    @ApiModelProperty(value = "用户ID",example = "rikky",required =true)
    private String userId;

    @ApiModelProperty(value = "备注",example = "rikky123",required =true)
    private String remark;


    @ApiModelProperty(value = "测试命令",example = "ped",required =true)
    private String testCommand;


}
