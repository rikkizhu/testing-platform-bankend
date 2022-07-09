package com.platform.testing.dto.jenkins;

import com.platform.testing.dto.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @program: QueryTestJenkinsListDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-20 17:18
 **/
@ApiModel(value="查询Jenkins信息列表对象")
@Data
public class QueryTestJenkinsListDto extends BaseDto {

    @ApiModelProperty(value="Jenkins名称")
    private String name;

    @ApiModelProperty(value="创建者id(客户端传值无效，以token数据为准)")
    private Integer createUserId;
}
