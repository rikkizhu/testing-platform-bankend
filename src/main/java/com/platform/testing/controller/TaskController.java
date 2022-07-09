package com.platform.testing.controller;

import com.platform.testing.common.*;
import com.platform.testing.dto.RequestInfoDto;
import com.platform.testing.dto.task.AddTestTaskDto;
import com.platform.testing.dto.task.QueryTestTaskListDto;
import com.platform.testing.dto.task.TestTaskDto;
import com.platform.testing.dto.testcase.StartTestDto;
import com.platform.testing.dto.testcase.UpdateTestTaskDto;
import com.platform.testing.dto.testcase.UpdateTestTaskStatusDto;
import com.platform.testing.entity.TestTask;
import com.platform.testing.service.TestTaskService;
import com.platform.testing.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

/**
 * @program: TaskController
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-15 16:49
 **/

@Api(tags = "测试任务管理模块")
@RestController
@RequestMapping("task")
@Slf4j
public class TaskController {
    @Autowired
    private TokenDb tokenDb;

    @Autowired
    private TestTaskService testTaskService;

    @ApiModelProperty("添加测试任务接口")
    @PostMapping
    public ResultDto saveTask(HttpServletRequest request, @RequestBody TestTaskDto testTaskDto) {

        List<Integer> caseIdList = testTaskDto.getCaseIdList();
        if (Objects.isNull(caseIdList) || caseIdList.size() == 0) {
            return ResultDto.fail("测试用例数据不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        AddTestTaskDto testTask = testTaskDto.getTestTask();

//        if (Objects.isNull(testTask)) {
//            testTask = new AddTestTaskDto();
//            testTaskDto.setTestTask(testTask);
//        }
//        testTask.setName("系统默认");
//        testTask.setCreateUserId(tokenDto.getUserId());
//        testTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        if (Objects.isNull(testTask)) {
            testTask = new AddTestTaskDto();
            testTaskDto.setTestTask(testTask);

            testTask.setName("系统默认");
        }

        if (Objects.isNull(testTask.getName())) {
            testTask.setName("系统默认");
        }
        testTask.setCreateUserId(tokenDto.getUserId());
        testTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());

        return testTaskService.save(testTaskDto, 1);

    }

    @ApiModelProperty("执行测试任务接口")
    @PostMapping("/start")
    public ResultDto startTest(HttpServletRequest request,
                               @RequestBody StartTestDto startTestDto) throws IOException, URISyntaxException {

        //参数校验
        if (Objects.isNull(startTestDto)) {
            return ResultDto.fail("参数不能为空");
        }
        if (Objects.isNull(startTestDto.getTaskId())) {
            return ResultDto.fail("任务id不能为空");
        }

        TestTask testTask = new TestTask();
        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        testTask.setCreateUserId(tokenDto.getUserId());
        testTask.setTestJenkinsId(tokenDto.getDefaultJenkinsId());
        testTask.setTestCommand(startTestDto.getTestCommand());
        testTask.setId(startTestDto.getTaskId());

        String url = request.getRequestURL().toString();
        url = StrUtil.getHostAndPort(url);

        RequestInfoDto requestInfoDto = new RequestInfoDto();
        requestInfoDto.setBaseUrl(url);
        requestInfoDto.setRequestUrl(url);
        requestInfoDto.setToken(tokenDto.getToken());

        return testTaskService.startTask(tokenDto, requestInfoDto, testTask);

    }

    @ApiModelProperty("修改测试任务状态")
    @PostMapping("status")
    public ResultDto updateStatus(HttpServletRequest request,
                                  @RequestBody UpdateTestTaskStatusDto updateTestTaskStatusDto) {
        if (Objects.isNull(updateTestTaskStatusDto)) {
            return ResultDto.fail("修改测试任务状态信息不能为空");
        }

        Integer taskId = updateTestTaskStatusDto.getTaskId();
        String buildUrl = updateTestTaskStatusDto.getBuildUrl();
        Integer status = updateTestTaskStatusDto.getStatus();

        if (Objects.isNull(taskId)) {
            return ResultDto.success("任务id不能为空");
        }

        if (StringUtils.isEmpty(buildUrl)) {
            return ResultDto.success("任务构建地址不能为空");
        }

        if (StringUtils.isEmpty(status)) {
            return ResultDto.success("任务状态码不能为空");
        }


        TestTask testTask = new TestTask();
        testTask.setId(taskId);
        testTask.setBuildUrl(buildUrl);
        testTask.setStatus(status);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        testTask.setCreateUserId(tokenDto.getUserId());

        return testTaskService.updateStatus(testTask);
    }

    @ApiOperation(value = "修改测试任务")
    @PutMapping
    public ResultDto<TestTask> update(HttpServletRequest request, @RequestBody UpdateTestTaskDto updateTestTaskDto) {

        if (Objects.isNull(updateTestTaskDto)) {
            return ResultDto.fail("测试任务信息不能为空");
        }

        Integer taskId = updateTestTaskDto.getId();
        String name = updateTestTaskDto.getName();

        if (Objects.isNull(taskId)) {
            return ResultDto.fail("任务id不能为空");
        }


        if (StringUtils.isEmpty(name)) {
            return ResultDto.fail("任务名称不能为空");
        }

        TestTask testTask = new TestTask();
        BeanUtils.copyProperties(updateTestTaskDto, testTask);

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));
        testTask.setCreateUserId(tokenDto.getUserId());

        return testTaskService.update(testTask);
    }

    @ApiOperation(value = "根据任务Id查询")
    @GetMapping("/{taskId}")
    public ResultDto<TestTask> getById(HttpServletRequest request, @PathVariable Integer taskId) {

        if (Objects.isNull(taskId)) {
            return ResultDto.success("任务Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        return testTaskService.getById(taskId, tokenDto.getUserId());
    }

    @ApiOperation(value = "根据任务Id删除")
    @DeleteMapping("/{taskId}")
    public ResultDto<TestTask> delete(HttpServletRequest request, @PathVariable Integer taskId) {

        if (Objects.isNull(taskId)) {
            return ResultDto.success("任务Id不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        return testTaskService.delete(taskId, tokenDto.getUserId());
    }

    @ApiOperation(value = "列表查询")
    @GetMapping("/list")
    public ResultDto<PageTableResponse<TestTask>> list(HttpServletRequest request, PageTableRequest<QueryTestTaskListDto> pageTableRequest) {

        if(Objects.isNull(pageTableRequest)){
            return ResultDto.success("列表查询参数不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        //将当前用户id作为查询条件，防止用户数据混乱
        pageTableRequest.getParams().put("createUserId", tokenDto.getUserId());

        return testTaskService.list(pageTableRequest);

    }
}
