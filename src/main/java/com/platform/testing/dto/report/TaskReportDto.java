package com.platform.testing.dto.report;

import com.platform.testing.dto.BaseDto;
import com.platform.testing.dto.task.TaskDataDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @program: TaskReportDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-23 15:12
 **/

@ApiModel(value="执行测试任务类",description="请求参数类" )
@Data
public class TaskReportDto extends BaseDto {

    /**
     * 任务总和
     */
    @ApiModelProperty(value="任务总和",required=true)
    @NotNull
    private Integer taskSum;

    /**
     * 任务数据对象
     */
    @ApiModelProperty(value="任务数据对象", required = true)
    private List<TaskDataDto> taskDataDtoList;

}
