package com.platform.testing.dto.task;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @program: TestTaskDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-21 21:02
 **/

@ApiModel(value="新增测试任务类",description="请求参数类" )
@Data
public class TestTaskDto {
    @ApiModelProperty(value="测试任务信息",required=true)
    private AddTestTaskDto testTask;

    @ApiModelProperty(value="测试用例的id列表", example="12",required=true)
    private List<Integer> caseIdList;
}
