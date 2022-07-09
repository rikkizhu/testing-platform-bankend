package com.platform.testing.common;

import lombok.Data;

/**
 * @program: TokenDto
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-19 19:39
 **/

@Data
public class TokenDto {
    private Integer userId;
    private String userName;
    private Integer defaultJenkinsId;
    private String token;
}
