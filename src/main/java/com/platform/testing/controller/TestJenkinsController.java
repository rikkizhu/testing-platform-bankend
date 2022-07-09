package com.platform.testing.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.testing.common.*;
import com.platform.testing.dto.jenkins.AddTestJenkinsDto;
import com.platform.testing.dto.jenkins.UpdateTestJenkinsDto;
import com.platform.testing.entity.TestJenkins;
import com.platform.testing.service.TestJenkinsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @program: TestJenkinsController
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-20 11:17
 **/

@Api(tags = "Jenkins管理模块")
@RestController
@RequestMapping("jenkins")
@Slf4j
public class TestJenkinsController {
    @Autowired
    private TestJenkinsService testJenkinsService;

    @Autowired
    private TokenDb tokenDb;

    @ApiOperation("添加Jenkins接口")
    @PostMapping()
    public ResultDto save(HttpServletRequest request, @RequestBody AddTestJenkinsDto addTestJenkinsDto) {
        TestJenkins testJenkins = new TestJenkins();
        log.info("AddTestJenkinsDto===" + JSONObject.toJSONString(addTestJenkinsDto));

        //校验Jenkins的baseUrl为空，为空给出提示
        if (StringUtils.isEmpty(addTestJenkinsDto.getUrl())) {
            return ResultDto.fail("baseUrl不能为空");
        }

        //从客户端强求的header中获取token
        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        String commandRunCaseSuffix = addTestJenkinsDto.getCommandRunCaseSuffix();
        if (!Objects.isNull(commandRunCaseSuffix)) {
            commandRunCaseSuffix = commandRunCaseSuffix.replace(".", "");
            addTestJenkinsDto.setCommandRunCaseSuffix(commandRunCaseSuffix);
        }


        BeanUtils.copyProperties(addTestJenkinsDto, testJenkins);

        testJenkins.setCreateUserId(tokenDto.getUserId());

        log.info("testJenkins===" + JSONObject.toJSONString(testJenkins));

        return testJenkinsService.save(tokenDto,testJenkins);
    }

    @ApiOperation("Jenkins列表查询")
    @GetMapping()
    public ResultDto<PageTableResponse<TestJenkins>> list(HttpServletRequest request,
                                                          PageTableRequest pageTableRequest) {
        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        //将当前用户id作为查询条件，防止用户数据混乱
        pageTableRequest.getParams().put("createUserId", tokenDto.getUserId());

        return testJenkinsService.list(pageTableRequest);
    }

    @ApiOperation("修改Jenkins")
    @PutMapping
    public ResultDto<TestJenkins> save(HttpServletRequest request, @RequestBody UpdateTestJenkinsDto updateTestJenkinsDto) {
        log.info("AddTestJenkinsDto===" + JSONObject.toJSONString(updateTestJenkinsDto));

        if (Objects.isNull(updateTestJenkinsDto)) {
            return ResultDto.fail("Jenkins信息不能为空");
        }

        Integer jenkinsId = updateTestJenkinsDto.getId();
        String name = updateTestJenkinsDto.getName();

        if (Objects.isNull(jenkinsId)) {
            return ResultDto.success("JenkinsId不能为空");
        }

        if (StringUtils.isEmpty(name)) {
            return ResultDto.success("Jenkins名称不能为空");
        }

        TestJenkins testJenkins = new TestJenkins();
        BeanUtils.copyProperties(updateTestJenkinsDto, testJenkins);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        testJenkins.setCreateUserId(tokenDto.getUserId());

        String commandRunCaseSuffix = updateTestJenkinsDto.getCommandRunCaseSuffix();

        if (!StringUtils.isEmpty(commandRunCaseSuffix)) {
            testJenkins.setCommandRunCaseSuffix(commandRunCaseSuffix.replace(".", ""));
        }

        ResultDto<TestJenkins> resultDto = testJenkinsService.update(tokenDto, testJenkins);

        return resultDto;

    }


    @ApiOperation(value = "根据jenkinsId删除")
    @DeleteMapping("/{jenkinsId}")
    public ResultDto<TestJenkins> delete(HttpServletRequest request, @PathVariable Integer jenkinsId) {
        log.info("根据jenkinsId删除-入参= "+ jenkinsId);

        if (Objects.isNull(jenkinsId)){
            return ResultDto.success("Jenkins不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        ResultDto<TestJenkins> resultDto = testJenkinsService.delete(jenkinsId, tokenDto);

        return resultDto;
    }

    @ApiOperation(value = "根据jenkinsId查询")
    @GetMapping("/{jenkinsId}")
    public ResultDto<TestJenkins> getById(HttpServletRequest request, @PathVariable Integer jenkinsId){
        log.info("根据jenkinsId查询-入参= "+ jenkinsId);

        if(Objects.isNull(jenkinsId)){
            return ResultDto.success("JenkinsId不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        ResultDto<TestJenkins> resultDto = testJenkinsService.getById(jenkinsId, tokenDto.getUserId());

        return resultDto;
    }



}
