package com.platform.testing.dto.jenkins;

import com.platform.testing.common.TokenDto;
import com.platform.testing.entity.TestJenkins;
import com.platform.testing.entity.TestUser;
import lombok.Data;

import java.util.Map;

/**
 * @program: OperateJenkinsJobDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-22 16:36
 **/
@Data
public class OperateJenkinsJobDto {

    private TokenDto tokenDto;


    private TestJenkins testJenkins;

    //构建参数
    private Map<String, String> params;

    private TestUser testUser;

}
