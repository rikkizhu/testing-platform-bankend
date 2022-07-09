package com.platform.testing.dto;

import com.platform.testing.entity.BaseEntityNew;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "用户注册类",description = "请求类")
@Data
public class AddTestUserDto extends BaseEntityNew {
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名",example = "rikky",required = true)
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码",example = "rikky",required = true)
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱",example = "rikky",required = true)
    private String email;


}