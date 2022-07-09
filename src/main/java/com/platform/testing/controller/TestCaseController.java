package com.platform.testing.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.testing.common.*;
import com.platform.testing.dto.testcase.AddTestCaseDto;
import com.platform.testing.dto.testcase.QueryTestCaseListDto;
import com.platform.testing.dto.testcase.UpdateTestCaseDto;
import com.platform.testing.entity.TestCase;
import com.platform.testing.service.TestCaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

/**
 * @program: TestCaseController
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-15 16:49
 **/

@Api(tags = "测试用例管理模块")
@RestController
@RequestMapping("case")
@Slf4j
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    @Autowired
    private TokenDb tokenDb;

    @ApiOperation("文件类型测试用例上传接口")
    @PostMapping("file")
    public ResultDto saveFile(HttpServletRequest request,
                              @RequestParam("caseFile") MultipartFile caseFile,
                              AddTestCaseDto addTestCaseDto) throws IOException {

        log.info("新增文件测试用例，请求入参===" + JSONObject.toJSONString(addTestCaseDto));

        if (Objects.isNull(addTestCaseDto)) {
            return ResultDto.fail("请求参数不能为空");
        }

        if (Objects.isNull(caseFile)) {
            return ResultDto.fail("测试用例文件不能为空");
        }
        if (StringUtils.isEmpty(addTestCaseDto.getCaseName())) {
            return ResultDto.fail("测试用例名称不能为空");
        }

        InputStream inputStream = caseFile.getInputStream();
        String caseDataStr = IOUtils.toString(inputStream);
        inputStream.close();

//        File file = new File("/Users/zhuruiqi/Desktop/testcase/caseData.txt");
//        caseFile.transferTo(file);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        TestCase testCase = new TestCase();
        testCase.setCreateUserId(tokenDto.getUserId());

        BeanUtils.copyProperties(addTestCaseDto, testCase);
        testCase.setCaseData(caseDataStr);

        ResultDto resultDto = testCaseService.save(testCase);

        return resultDto;
    }

    @ApiOperation(value = "批量新增测试用例", notes = "仅用于测试用户")
    @PostMapping("text")
    public ResultDto saveText(HttpServletRequest request, @RequestBody AddTestCaseDto addTestCaseDto) {
        log.info("新增文本测试用例，请求入参===" + JSONObject.toJSONString(addTestCaseDto));

        if (Objects.isNull(addTestCaseDto)) {
            return ResultDto.fail("请求参数不能为空");
        }

        if (Objects.isNull(addTestCaseDto.getCaseData())) {
            return ResultDto.fail("测试用例文件不能为空");
        }
        if (StringUtils.isEmpty(addTestCaseDto.getCaseName())) {
            return ResultDto.fail("测试用例名称不能为空");
        }

        TestCase testCase = new TestCase();

        BeanUtils.copyProperties(addTestCaseDto, testCase);
        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        testCase.setCreateUserId(tokenDto.getUserId());

        ResultDto resultDto = testCaseService.save(testCase);
        return resultDto;
    }

    @ApiOperation(value = "根据测试用例id查询case原始数据")
    @GetMapping("data/{caseId}")
    public String getCaseDataById(HttpServletRequest request, @PathVariable Integer caseId) {
        log.info("===根据用户id和caseId查询case原始数据-请求入参===" + caseId);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        String caseData = testCaseService.getCaseDataById(tokenDto.getUserId(), caseId);

        log.info("===根据用户id和caseId查询case原始数据-请求出参===" + caseData);
        return caseData;
    }

    @ApiOperation(value = "修改测试用例")
    @PutMapping
    public ResultDto<TestCase> update(HttpServletRequest request, @RequestBody UpdateTestCaseDto updateTestCaseDto) {
        log.info("修改测试用例，请求入参===" + JSONObject.toJSONString(updateTestCaseDto));

        if (Objects.isNull(updateTestCaseDto)) {
            return ResultDto.success("测试用例信息不能为空");
        }

        Integer caseId = updateTestCaseDto.getId();
        String caseName = updateTestCaseDto.getCaseName();

        if (Objects.isNull(caseId)) {
            return ResultDto.success("测试用例id不能为空");
        }

        if (StringUtils.isEmpty(caseName)) {
            return ResultDto.success("测试用例名称不能为空");
        }

        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(updateTestCaseDto, testCase);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        testCase.setCreateUserId(tokenDto.getUserId());

        ResultDto<TestCase> resultDto = testCaseService.update(testCase);

        return resultDto;

    }

    @ApiOperation(value = "根据caseId查询")
    @GetMapping("/{caseId}")
    public ResultDto<TestCase> getById(HttpServletRequest request, @PathVariable Integer caseId) {
        log.info("根据caseId查询-入参= " + caseId);

        if (Objects.isNull(caseId)) {
            return ResultDto.success("caseId不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        ResultDto<TestCase> resultDto = testCaseService.getById(caseId, tokenDto.getUserId());

        return resultDto;
    }

    @ApiOperation(value = "根据caseId删除")
    @DeleteMapping("/{caseId}")
    public ResultDto<TestCase> delete(HttpServletRequest request, @PathVariable Integer caseId){

        log.info("根据caseId删除-入参= "+ caseId);

        if(Objects.isNull(caseId)){
            return ResultDto.success("caseId不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        ResultDto<TestCase> resultDto = testCaseService.delete(caseId, tokenDto.getUserId());
        return resultDto;
    }

    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<TestCase>> list(HttpServletRequest request, PageTableRequest<QueryTestCaseListDto> pageTableRequest){
        log.info("测试用例列表查询-入参= "+ JSONObject.toJSONString(pageTableRequest));

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        //将当前用户id作为查询条件，防止用户数据混乱
        pageTableRequest.getParams().put("createUserId",tokenDto.getUserId());

        return testCaseService.list(pageTableRequest);

    }


}
