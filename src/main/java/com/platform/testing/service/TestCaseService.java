package com.platform.testing.service;

import com.platform.testing.common.PageTableRequest;
import com.platform.testing.common.PageTableResponse;
import com.platform.testing.common.ResultDto;
import com.platform.testing.dto.testcase.QueryTestCaseListDto;
import com.platform.testing.entity.TestCase;

/**
 * @program: TestUserService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-16 10:10
 **/
public interface TestCaseService {
     ResultDto<TestCase> save(TestCase testCase );


     String getCaseDataById(Integer createUserId, Integer caseId);

     ResultDto<TestCase> update(TestCase testCase);

     ResultDto<TestCase> getById(Integer caseId, Integer createUserId);

     ResultDto<TestCase> delete(Integer caseId, Integer createUserId);

    ResultDto<PageTableResponse<TestCase>> list(PageTableRequest<QueryTestCaseListDto> pageTableRequest);
}
