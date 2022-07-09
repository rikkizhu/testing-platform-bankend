package com.platform.testing.service;

import com.platform.testing.common.PageTableRequest;
import com.platform.testing.common.PageTableResponse;
import com.platform.testing.common.ResultDto;
import com.platform.testing.common.TokenDto;
import com.platform.testing.dto.RequestInfoDto;
import com.platform.testing.dto.task.QueryTestTaskListDto;
import com.platform.testing.dto.task.TestTaskDto;
import com.platform.testing.entity.TestTask;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @program: TestUserService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-16 10:10
 **/
public interface TestTaskService {

     ResultDto<TestTask> save(TestTaskDto testTaskDto, Integer taskType);

    ResultDto<TestTask> startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, TestTask testTask) throws IOException, URISyntaxException;

    ResultDto updateStatus(TestTask testTask);

    ResultDto<TestTask> update(TestTask testTask);

    ResultDto<TestTask> getById(Integer taskId, Integer createUserId);

    ResultDto<TestTask> delete(Integer taskId, Integer createUserId);

    ResultDto<PageTableResponse<TestTask>> list(PageTableRequest<QueryTestTaskListDto> pageTableRequest);
}
