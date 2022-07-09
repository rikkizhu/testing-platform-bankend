package com.platform.testing.controller;

import com.platform.testing.common.ResultDto;
import com.platform.testing.common.TokenDb;
import com.platform.testing.common.TokenDto;
import com.platform.testing.common.UserBaseStr;
import com.platform.testing.dto.report.AllureReportDto;
import com.platform.testing.dto.report.TaskReportDto;
import com.platform.testing.entity.TestTask;
import com.platform.testing.service.TestReportService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @program: ReportController
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-23 12:32
 **/

@Slf4j
@Api(tags = "报告管理")
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private TestReportService testReportService;

    @Autowired
    private TokenDb tokenDb;


    @ApiOperation(value = "获取allure报告")
    @GetMapping("/allureReport/{taskId}")
    public ResultDto<AllureReportDto> getAllureReport(HttpServletRequest request, @PathVariable Integer taskId) {

        if (Objects.isNull(taskId)) {
            return ResultDto.success("任务id不能为空");
        }

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        return testReportService.getAllureReport(tokenDto, taskId);

    }

    /**
     * 根据任务类型获取任务统计信息 - 饼状图
     * @return
     */
    @ApiOperation(value = "根据任务类型获取任务统计信息 - 饼状图")
    @GetMapping("/taskByType")
    public ResultDto<TaskReportDto> getTaskByType(HttpServletRequest request){

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

       return testReportService.getTaskByType(tokenDto);
    }

    /**
     * 根据任务状态获取任务统计信息 - 饼状图
     * @return
     */
    @ApiOperation(value = "根据任务状态获取任务统计信息 - 饼状图")
    @GetMapping("/taskByStatus")
    public ResultDto<TaskReportDto> getTaskByStatus(HttpServletRequest request){

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        return testReportService.getTaskByStatus(tokenDto);
    }

    /**
     * 任务中用例的数量统计信息 - 折线图
     * @param request
     * @param start 按时间倒叙开始序号
     * @param end 按时间倒叙结束序号
     * @return
     */
    @ApiOperation(value = "任务中用例的数量统计信息 - 折线图")
    @GetMapping("/taskByCaseCount")
    public ResultDto<List<TestTask>> getTaskByCaseCount(HttpServletRequest request
            , @RequestParam(value = "start",required = false) Integer start
            , @RequestParam(value = "end",required = false) Integer end){

        TokenDto tokenDto = tokenDb.getUserInfo(request.getHeader(UserBaseStr.LOGIN_TOKEN));

        return  testReportService.getTaskByCaseCount(tokenDto, start, end);
    }

}
