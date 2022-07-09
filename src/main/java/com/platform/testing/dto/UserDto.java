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
@ApiModel(value = "用户登录类",description = "请求类")
@Data
public class UserDto {

    @ApiModelProperty(value = "用户名",example = "rikky",required =true)
    private String name;

    @ApiModelProperty(value = "用户密码",example = "rikky123",required =true)
    private String pwd;

//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPwd() {
//        return pwd;
//    }
//
//    public void setPwd(String pwd) {
//        this.pwd = pwd;
//    }
}
