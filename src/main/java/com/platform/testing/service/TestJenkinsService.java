package com.platform.testing.service;

import com.platform.testing.common.PageTableRequest;
import com.platform.testing.common.PageTableResponse;
import com.platform.testing.common.ResultDto;
import com.platform.testing.common.TokenDto;
import com.platform.testing.dto.jenkins.QueryTestJenkinsListDto;
import com.platform.testing.entity.TestJenkins;

/**
 * @program: TestUserService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-16 10:10
 **/
public interface TestJenkinsService {
    ResultDto<TestJenkins> save(TokenDto tokenDto, TestJenkins testJenkins);

    ResultDto<PageTableResponse<TestJenkins>> list(PageTableRequest<QueryTestJenkinsListDto> pageTableRequest);

    ResultDto<TestJenkins> update(TokenDto tokenDto, TestJenkins testJenkins);

    ResultDto<TestJenkins> delete(Integer jenkinsId, TokenDto tokenDto);

    ResultDto<TestJenkins> getById(Integer jenkinsId, Integer createUserId);
}
