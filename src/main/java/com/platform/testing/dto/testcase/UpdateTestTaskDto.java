package com.platform.testing.dto.testcase;

import com.platform.testing.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: UpdateTestTaskDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-22 16:21
 **/
@ApiModel(value="修改任务对象")
@Data
public class UpdateTestTaskDto extends BaseEntityNew {
    /**
     * ID
     */
    @ApiModelProperty(value="任务主键",required=true)
    private Integer id;

    /**
     * 名称
     */
    @ApiModelProperty(value="任务名称",required=true)
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty(value="任务备注")
    private String remark;

}