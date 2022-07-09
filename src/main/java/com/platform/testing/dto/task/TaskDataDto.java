package com.platform.testing.dto.task;

import com.platform.testing.dto.BaseDto;
import org.jetbrains.annotations.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: TaskDataDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-23 15:13
 **/
@ApiModel(value = "执行测试任务类", description = "请求参数类")
@Data
public class TaskDataDto extends BaseDto {

    /**
     * 任务数量
     */
    @ApiModelProperty(value = "任务数量", required = true)
    @NotNull
    private Integer taskCount;

    /**
     * 分类的key
     */
    @ApiModelProperty(value = "分类的key", required = true)
    private Integer taskKey;

    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    private String desc;

}