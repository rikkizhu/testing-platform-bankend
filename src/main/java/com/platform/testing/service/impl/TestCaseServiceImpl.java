package com.platform.testing.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.platform.testing.common.PageTableRequest;
import com.platform.testing.common.PageTableResponse;
import com.platform.testing.common.ResultDto;
import com.platform.testing.dao.TestCaseMapper;
import com.platform.testing.dto.testcase.QueryTestCaseListDto;
import com.platform.testing.entity.TestCase;
import com.platform.testing.service.TestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: com.platform.testing.service.impl.TestCaseService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-21 15:38
 **/
@Slf4j
@Service
public class TestCaseServiceImpl implements TestCaseService {
    @Autowired
    private TestCaseMapper testCaseMapper;

    @Override
    public ResultDto<TestCase> save(TestCase testCase) {
        testCase.setCreateTime(new Date());
        testCase.setUpdateTime(new Date());
        testCase.setDelFlag(1);

        testCaseMapper.insertUseGeneratedKeys(testCase);
        return ResultDto.success("成功", testCase);
    }


    @Override
    public String getCaseDataById(Integer createUserId, Integer caseId) {
        if (Objects.isNull(caseId)) {
            return "用例id为空";
        }

        TestCase queryTestCase = new TestCase();
        queryTestCase.setCreateUserId(createUserId);
        queryTestCase.setId(caseId);

        log.info("=====根据测试用例id查询case原始数据-查库入参====：" + JSONObject.toJSONString(queryTestCase));

        TestCase resultTestCase = testCaseMapper.selectOne(queryTestCase);

        if (Objects.isNull(resultTestCase)) {
            return "用例数据未查到";
        }
        if (StringUtils.isEmpty(resultTestCase.getCaseData())) {
            return "用例原始数据未查到";
        }

        return resultTestCase.getCaseData();

    }

    @Override
    public ResultDto<TestCase> update(TestCase testCase) {
        TestCase queryTestCase = new TestCase();

        queryTestCase.setId(testCase.getId());
        queryTestCase.setCreateUserId(testCase.getCreateUserId());
        queryTestCase.setDelFlag(1);

        TestCase result = testCaseMapper.selectOne(queryTestCase);

        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试用例信息");
        }

        testCase.setCreateTime(result.getCreateTime());
        testCase.setUpdateTime(new Date());
        testCase.setDelFlag(1);

        testCaseMapper.updateByPrimaryKey(testCase);

        return ResultDto.success("成功");
    }

    @Override
    public ResultDto<TestCase> getById(Integer caseId, Integer createUserId) {
        TestCase queryTestCase = new TestCase();

        queryTestCase.setId(caseId);
        queryTestCase.setCreateUserId(createUserId);
        queryTestCase.setDelFlag(1);

        TestCase result = testCaseMapper.selectOne(queryTestCase);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试用例信息");
        }

        return ResultDto.success("成功", result);
    }

    @Override
    public ResultDto<TestCase> delete(Integer caseId, Integer createUserId) {

        TestCase queryTestCase = new TestCase();

        queryTestCase.setId(caseId);
        queryTestCase.setCreateUserId(createUserId);
        queryTestCase.setDelFlag(1);

        TestCase result = testCaseMapper.selectOne(queryTestCase);

        //如果为空，则提示
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试用例信息");
        }
        result.setDelFlag(0);
        testCaseMapper.deleteByPrimaryKey(caseId);

        return ResultDto.success("成功");
    }

    @Override
    public ResultDto<PageTableResponse<TestCase>> list(PageTableRequest<QueryTestCaseListDto> pageTableRequest) {
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();
        Map params = pageTableRequest.getParams();

        int count = testCaseMapper.count(params);

        List<TestCase> testCaseList = testCaseMapper
                .list(params, (pageNum - 1) * pageSize, pageSize);

        PageTableResponse response = new PageTableResponse();
        response.setRecordsTotal(count);
        response.setData(testCaseList);

        return ResultDto.success("成功", response);
    }
}
