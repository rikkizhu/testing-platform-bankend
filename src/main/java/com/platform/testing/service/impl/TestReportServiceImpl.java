package com.platform.testing.service.impl;

import com.platform.testing.common.ResultDto;
import com.platform.testing.common.TokenDto;
import com.platform.testing.common.UserBaseStr;
import com.platform.testing.dao.TestJenkinsMapper;
import com.platform.testing.dao.TestTaskMapper;
import com.platform.testing.dto.report.AllureReportDto;
import com.platform.testing.dto.report.TaskReportDto;
import com.platform.testing.dto.task.TaskDataDto;
import com.platform.testing.entity.TestJenkins;
import com.platform.testing.entity.TestTask;
import com.platform.testing.service.TestReportService;
import com.platform.testing.util.ReportUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @program: TestReportServiceImpl
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-23 12:34
 **/

@Slf4j
@Service
public class TestReportServiceImpl implements TestReportService {
    @Autowired
    private TestTaskMapper testTaskMapper;

    @Autowired
    private TestJenkinsMapper testJenkinsMapper;


    @Override
    public ResultDto<AllureReportDto> getAllureReport(TokenDto tokenDto, Integer taskId) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(taskId);
        queryTestTask.setCreateUserId(tokenDto.getUserId());

        TestTask resultTestTask = testTaskMapper.selectOne(queryTestTask);

        if (Objects.isNull(resultTestTask)) {
            return ResultDto.fail("测试任务不存在 " + taskId);
        }

        String buildUrl = resultTestTask.getBuildUrl();

        if (StringUtils.isEmpty(buildUrl)) {
            return ResultDto.fail("测试任务的构建地址不存在 " + taskId);
        }

        Integer testJenkinsId = resultTestTask.getTestJenkinsId();


        if (Objects.isNull(testJenkinsId)) {
            return ResultDto.fail("测试任务的jenkinsId不存在 " + taskId);
        }

        TestJenkins queryTestJenkins = new TestJenkins();
        queryTestJenkins.setId(testJenkinsId);
        queryTestJenkins.setCreateUserId(tokenDto.getUserId());

        TestJenkins resultTestJenkins = testJenkinsMapper.selectOne(queryTestJenkins);

        String allureReportUrl = ReportUtil.getAllureReportUrl(buildUrl, resultTestJenkins, true);

        AllureReportDto allureReportDto = new AllureReportDto();
        allureReportDto.setTaskId(taskId);
        allureReportDto.setAllureReportUrl(allureReportUrl);

        return ResultDto.success("成功", allureReportDto);
    }

    @Override
    public ResultDto<TaskReportDto> getTaskByType(TokenDto tokenDto) {
        TaskReportDto taskReportDto = new TaskReportDto();

        List<TaskDataDto> taskDataDtoList = testTaskMapper.getTaskByType(tokenDto.getUserId());

        if (Objects.isNull(taskDataDtoList) || taskDataDtoList.size() == 0) {
            ResultDto.fail("无数据");
        }

        List<TaskDataDto> newtTaskDataDtoList = new ArrayList<>();
        Integer taskSum = 0;

        for (TaskDataDto taskDataDto : taskDataDtoList) {

            Integer taskKey = taskDataDto.getTaskKey();
            if (Objects.isNull(taskKey)) {
                taskKey = 0;
            }

            if (0 == taskKey) {
                taskDataDto.setDesc("无匹配任务");
            }
            if (UserBaseStr.TASK_TYPE_EXECUTE.equals(taskKey)) {
                taskDataDto.setDesc("普通测试任务");
            }
            if (UserBaseStr.TASK_TYPE_ONECLICK.equals(taskKey)) {
                taskDataDto.setDesc("一键执行测试的任务");
            }

            taskSum = taskSum + taskDataDto.getTaskCount();

            newtTaskDataDtoList.add(taskDataDto);
        }

        taskReportDto.setTaskSum(taskSum);
        taskReportDto.setTaskDataDtoList(newtTaskDataDtoList);

        return ResultDto.success("成功", taskReportDto);
    }

    @Override
    public ResultDto<TaskReportDto> getTaskByStatus(TokenDto tokenDto) {
        TaskReportDto taskReportDto = new TaskReportDto();

        Integer taskSum = 0;

        List<TaskDataDto> taskDataDtoList = testTaskMapper.getTaskByStatus(tokenDto.getUserId());

        if (Objects.isNull(taskDataDtoList) || taskDataDtoList.size() == 0) {
            ResultDto.fail("无数据");
        }

        List<TaskDataDto> newtTaskDataDtoList = new ArrayList<>();

        for (TaskDataDto taskDataDto : taskDataDtoList) {

            Integer taskKey = taskDataDto.getTaskKey();
            if (Objects.isNull(taskKey)) {
                taskKey = 0;
            }

            if (0 == taskKey) {
                taskDataDto.setDesc("无匹配任务");
            }
            if (UserBaseStr.STATUS_CREATE.equals(taskKey)) {
                taskDataDto.setDesc("新建");
            }
            if (UserBaseStr.STATUS_TESTING.equals(taskKey)) {
                taskDataDto.setDesc("执行中");
            }
            if (UserBaseStr.STATUS_DONE.equals(taskKey)) {
                taskDataDto.setDesc("已完成");
            }

            taskSum = taskSum + taskDataDto.getTaskCount();

            newtTaskDataDtoList.add(taskDataDto);

        }

        taskReportDto.setTaskSum(taskSum);
        taskReportDto.setTaskDataDtoList(newtTaskDataDtoList);

        return ResultDto.success("成功", taskReportDto);
    }

    @Override
    public ResultDto<List<TestTask>> getTaskByCaseCount(TokenDto tokenDto, Integer start, Integer end) {
        List<TestTask> testTaskList = testTaskMapper.getCaseCountByTask(tokenDto.getUserId(), start, end);

        if (Objects.isNull(testTaskList) || testTaskList.size() == 0) {
            return ResultDto.fail("无数据");
        }

        return ResultDto.success("成功", testTaskList);
    }

}
