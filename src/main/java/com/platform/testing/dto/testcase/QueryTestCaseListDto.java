package com.platform.testing.dto.testcase;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @program: QueryTestCaseListDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-21 19:19
 **/
@ApiModel(value="查询测试用例信息列表对象")
public class QueryTestCaseListDto {
    @ApiModelProperty(value="测试用例名称")
    private String caseName;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
