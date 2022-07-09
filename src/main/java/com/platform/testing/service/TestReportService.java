package com.platform.testing.service;

import com.platform.testing.common.ResultDto;
import com.platform.testing.common.TokenDto;
import com.platform.testing.dto.report.AllureReportDto;
import com.platform.testing.dto.report.TaskReportDto;
import com.platform.testing.entity.TestTask;

import java.util.List;

/**
 * @program: TestReportService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-23 12:32
 **/

public interface TestReportService {

    ResultDto<AllureReportDto> getAllureReport(TokenDto tokenDto, Integer taskId);

    ResultDto<TaskReportDto> getTaskByType(TokenDto tokenDto);

    ResultDto<TaskReportDto> getTaskByStatus(TokenDto tokenDto);

    ResultDto<List<TestTask>> getTaskByCaseCount(TokenDto tokenDto, Integer start, Integer end);
}
