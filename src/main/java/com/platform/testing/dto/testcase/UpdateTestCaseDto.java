package com.platform.testing.dto.testcase;

import com.platform.testing.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "添加测试用例对象")
@Data
public class UpdateTestCaseDto extends BaseEntityNew {
    /**
     * 主键
     */
    @ApiModelProperty(value="测试用例主键",required=true)
    private Integer id;

    /**
     * 测试数据
     */
    @ApiModelProperty(value = "测试用例数据",notes = "文件类型case时不传值",required =true)
    private String caseData;

    /**
     * 用例名称
     */
    @ApiModelProperty(value = "测试用例名称",required =true)
    private String caseName;

    /**
     * 备注
     */
    @ApiModelProperty(value = "测试用例备注")
    private String remark;



}