package com.platform.testing.service.impl;

import com.platform.testing.common.*;
import com.platform.testing.dao.*;
import com.platform.testing.dto.RequestInfoDto;
import com.platform.testing.dto.jenkins.OperateJenkinsJobDto;
import com.platform.testing.dto.task.AddTestTaskDto;
import com.platform.testing.dto.task.QueryTestTaskListDto;
import com.platform.testing.dto.task.TestTaskDto;
import com.platform.testing.entity.*;
import com.platform.testing.service.TestTaskService;
import com.platform.testing.util.JenkinsUtil;
import com.platform.testing.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * @program: com.platform.testing.service.impl.TestCaseService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-21 15:38
 **/
@Slf4j
@Service
public class TestTaskServiceImpl implements TestTaskService {
    @Autowired
    private TestJenkinsMapper testJenkinsMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private TestTaskMapper testTaskMapper;

    @Autowired
    private TestTaskCaseRelMapper testTaskCaseRelMapper;

    @Autowired
    private TestUserMapper testUserMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDto<TestTask> save(TestTaskDto testTaskDto, Integer taskType) {
        StringBuilder testCommand = new StringBuilder();

        AddTestTaskDto addTestTaskDto = testTaskDto.getTestTask();
        List<Integer> caseIdList = testTaskDto.getCaseIdList();

        if (Objects.isNull(caseIdList) || caseIdList.size() == 0) {
            return ResultDto.fail("测试用例数据不能为空");
        }

        Integer createUserId = addTestTaskDto.getCreateUserId();
        Integer testJenkinsId = addTestTaskDto.getTestJenkinsId();

        if (Objects.isNull(testJenkinsId)) {
            return ResultDto.fail("默认Jenkins未设置");
        }

        //根据用户默认Jenkins查询Jenkins信息,并做非空校验
        TestJenkins queryTestJenkins = new TestJenkins();
        queryTestJenkins.setId(testJenkinsId);
        queryTestJenkins.setCreateUserId(createUserId);

        TestJenkins resultTestJenkins = testJenkinsMapper.selectOne(queryTestJenkins);

        if (Objects.isNull(resultTestJenkins)) {
            return ResultDto.fail("默认Jenkins未设置");
        }

        //根据用户选择的测试用例id查询测试用例信息
        String idListStr = StrUtil.list2IdsStr(caseIdList);
        List<TestCase> testCaseList = testCaseMapper.selectByIds(idListStr);

        //生成测试命令
        makeTestCommand(testCommand, resultTestJenkins, testCaseList);

        //测试任务存库
        TestTask testTask = new TestTask();
        testTask.setName(addTestTaskDto.getName());
        testTask.setTestJenkinsId(addTestTaskDto.getTestJenkinsId());
        testTask.setCreateUserId(addTestTaskDto.getCreateUserId());
        testTask.setRemark(addTestTaskDto.getRemark());

        testTask.setTaskType(taskType);
        testTask.setTestCommand(testCommand.toString());
        testTask.setCaseCount(caseIdList.size());
        testTask.setStatus(UserBaseStr.STATUS_CREATE);
        testTask.setCreateTime(new Date());
        testTask.setUpdateTime(new Date());

        testTaskMapper.insert(testTask);

        //测试任务详情数据落库
        List<TestTaskCaseRel> testTaskCaseRelList = new ArrayList<>();
        for (Integer caseId : caseIdList) {
            TestTaskCaseRel testTaskCaseRel = new TestTaskCaseRel();
            testTaskCaseRel.setTaskId(testTask.getId());
            testTaskCaseRel.setCaseId(caseId);
            testTaskCaseRel.setCreateUserId(createUserId);
            testTaskCaseRel.setCreateTime(new Date());
            testTaskCaseRel.setUpdateTime(new Date());

            testTaskCaseRelList.add(testTaskCaseRel);
        }

        testTaskCaseRelMapper.insertList(testTaskCaseRelList);

        return ResultDto.success("成功", testTask);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TestTask> startTask(TokenDto tokenDto, RequestInfoDto requestInfoDto, TestTask testTask) throws IOException, URISyntaxException {
        //参数校验和默认Jenkins是否有效
        Integer defaultJenkinsId = tokenDto.getDefaultJenkinsId();
        if (Objects.isNull(defaultJenkinsId)) {
            return ResultDto.fail("默认Jenkins未设置");
        }

        Integer userId = tokenDto.getUserId();
        TestUser queryUser = new TestUser();
        queryUser.setId(userId);
        TestUser resultTestUser = testUserMapper.selectOne(queryUser);


        TestJenkins queryTestJenkins = new TestJenkins();
        queryTestJenkins.setId(defaultJenkinsId);
        queryTestJenkins.setCreateUserId(userId);
        TestJenkins resultTestJenkins = testJenkinsMapper.selectOne(queryTestJenkins);

        if (Objects.isNull(resultTestJenkins)) {
            return ResultDto.fail("默认Jenkins没有查询到");
        }

        //查询测试任务
        TestTask queryTestTask = new TestTask();
        queryTestTask.setCreateUserId(testTask.getCreateUserId());
        queryTestTask.setId(testTask.getId());

        TestTask resultTestTask = testTaskMapper.selectOne(queryTestTask);

        if (Objects.isNull(resultTestTask)) {
            return ResultDto.fail("任务未查到，请确认");
        }

        //获取测试命令并更新任务状态为执行中
        String testCommand = resultTestTask.getTestCommand();
        resultTestTask.setStatus(UserBaseStr.STATUS_TESTING);
        testTaskMapper.updateByPrimaryKeySelective(resultTestTask);

        //获取更新任务状态的回调地址updateStatusUrl
        /**
         * 形如
         * curl -X Post "http://localhost:8087/task/status " -H "Content-Type: application/json " -H "token: acab4db17fe7f898c9f5dd2f726a7f3c" -d '{"buildUrl":"${BUILD_URL}","taskId":80,"status":3}'
         */
        //修改任务执行状态
        StringBuilder updateTaskStatusUrl = JenkinsUtil.getUpdateTaskStatusUrl(requestInfoDto, testTask);

        //组建Jenkins构建参数
        Map map = new HashMap();
        map.put("BaseUrl", requestInfoDto.getBaseUrl());
        map.put("token", requestInfoDto.getToken());
        map.put("testCommand", testCommand);
        map.put("updateStatusData", updateTaskStatusUrl.toString());

        //调用Jenkins
        OperateJenkinsJobDto operateJenkinsJobDto = new OperateJenkinsJobDto();
        operateJenkinsJobDto.setTokenDto(tokenDto);
        operateJenkinsJobDto.setTestJenkins(resultTestJenkins);
        operateJenkinsJobDto.setParams(map);
        operateJenkinsJobDto.setTestUser(resultTestUser);

        ResultDto<TestUser> resultDto = JenkinsUtil.build2(operateJenkinsJobDto);

        if (resultDto.getResultCode() == 0) {
            ServiceException.throwEx(resultDto.getMessage());
        }

        TestUser resultTestUser1 = resultDto.getData();
        testUserMapper.updateByPrimaryKeySelective(resultTestUser1);

        return ResultDto.success("成功", resultTestTask);
    }

    @Override
    public ResultDto updateStatus(TestTask testTask) {
        TestTask queryTestTask = new TestTask();
        queryTestTask.setId(testTask.getId());
        queryTestTask.setCreateUserId(testTask.getCreateUserId());
        TestTask result = testTaskMapper.selectOne(queryTestTask);

        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        //如果任务已经完成，则不重复修改
        if (UserBaseStr.STATUS_DONE.equals(result.getStatus())) {
            return ResultDto.fail("测试任务已完成，无需修改");
        }

        result.setUpdateTime(new Date());

        //仅状态为已完成时修改
        if (UserBaseStr.STATUS_DONE.equals(testTask.getStatus())) {
            result.setBuildUrl(testTask.getBuildUrl());
            result.setStatus(UserBaseStr.STATUS_DONE);
            testTaskMapper.updateByPrimaryKey(result);
        }

        return ResultDto.success("成功");

    }

    @Override
    public ResultDto<TestTask> update(TestTask testTask) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(testTask.getId());
        queryTestTask.setCreateUserId(testTask.getCreateUserId());

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }

        result.setUpdateTime(new Date());
        result.setName(testTask.getName());
        result.setRemark(testTask.getRemark());

        testTaskMapper.updateByPrimaryKeySelective(result);

        return ResultDto.success("成功", result);

    }

    @Override
    public ResultDto<TestTask> getById(Integer taskId, Integer createUserId) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(taskId);
        queryTestTask.setCreateUserId(createUserId);

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            ResultDto.fail("未查到测试任务信息");
        }

        return ResultDto.success("成功", result);
    }

    @Override
    public ResultDto<TestTask> delete(Integer taskId, Integer createUserId) {
        TestTask queryTestTask = new TestTask();

        queryTestTask.setId(taskId);
        queryTestTask.setCreateUserId(createUserId);

        TestTask result = testTaskMapper.selectOne(queryTestTask);

        //如果为空，则提示，也可以直接返回成功
        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到测试任务信息");
        }
        testTaskMapper.deleteByPrimaryKey(taskId);

        return ResultDto.success("成功");
    }

    @Override
    public ResultDto<PageTableResponse<TestTask>> list(PageTableRequest<QueryTestTaskListDto> pageTableRequest) {

        //获取参数
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();
        Map params = pageTableRequest.getParams();

        //获取数据总数量
        int count = testTaskMapper.count(params);

        //获取数据列表
        List<TestTask> testTaskList = testTaskMapper
                .list(params, (pageNum - 1) * pageSize, pageSize);

        //给响应参数赋值
        PageTableResponse response = new PageTableResponse();
        response.setRecordsTotal(count);
        response.setData(testTaskList);

        //返回
        return ResultDto.success("成功", response);
    }

    private void makeTestCommand(StringBuilder testCommand, TestJenkins testJenkins, List<TestCase> testCaseList) {

        //校验
        if (Objects.isNull(testJenkins)) {
            ServiceException.throwEx("组装测试命令时，Jenkins信息为空");
        }
        if (Objects.isNull(testCaseList) || testCaseList.size() == 0) {
            throw new ServiceException("组装测试命令时，测试用例列表信息为空");
        }

        Integer commandRunCaseType = testJenkins.getCommandRunCaseType();
        String systemTestCommand = testJenkins.getTestCommand();

        if (StringUtils.isEmpty(systemTestCommand)) {
            throw new ServiceException("组装测试命令时，运行的测试命令信息为空");
        }

        //用例类型为空，则默认文本类型
        if (Objects.isNull(commandRunCaseType)) {
            commandRunCaseType = 1;
        }

        //打印测试目录
        testCommand.append("pwd").append("\n");

        /**
         * 文本类型处理方式
         * 形如
         *  mvn test Class1
         *  mvn test Class2
         *  mvn test Class3
         */
        if (commandRunCaseType == 1) {
            for (TestCase testCase : testCaseList) {
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //拼装测试数据
                testCommand.append(testCase.getCaseData())
                        .append("\n");
            }
        }


        /**
         * 文件类型处理方式
         *
         * 形如
         * curl -o xxx.yml ${BaseUrl}/case/data/${id} -H \"token: ${token}\" || true\n\n
         * hrun --alluredir=${WORKSPACE}/target/allure-results xxx.yml || true \n\n
         *
         *
         * curl -o xxx.yml ${BaseUrl}/case/data/${id} -H \"token: ${token}\" || true\n\n
         * hrun --alluredir=${WORKSPACE}/target/allure-results xxx.yml || true \n\n
         *
         *
         * curl -o xxx.yml ${BaseUrl}/case/data/${id} -H \"token: ${token}\" || true\n\n
         * hrun --alluredir=${WORKSPACE}/target/allure-results xxx.yml || true \n\n
         */
        if (commandRunCaseType == 2) {

            String commandRunCaseSuffix = testJenkins.getCommandRunCaseSuffix();

            if (StringUtils.isEmpty(commandRunCaseSuffix)) {
                throw new ServiceException("组装测试命令且case为文件时，测试用例后缀名不能为空");
            }

            for (TestCase testCase : testCaseList) {

                //拼装下载文件的curl命令
                makeCurlCommand(testCommand, testCase, commandRunCaseSuffix);
                testCommand.append("\n");
                //拼装命令前缀
                testCommand.append(systemTestCommand).append(" ");
                //平台测试用例名称
                testCommand.append(testCase.getCaseName())
                        //拼装.分隔符
                        .append(".")
                        //拼装case文件后缀
                        .append(commandRunCaseSuffix)
                        .append(" || true")
                        .append("\n");
            }
        }


        log.info("testCommand.toString()== " + testCommand.toString());


        testCommand.append("\n");
    }

    /**
     * 拼装下载文件的curl命令
     * 形如 curl -o xxx.yml ${BaseUrl}/case/data/${id} -H \"token: ${token}\" || true\n
     *
     * @param testCommand
     * @param testCase
     * @param commandRunCaseSuffix
     */
    private void makeCurlCommand(StringBuilder testCommand, TestCase testCase, String commandRunCaseSuffix) {

        //通过curl命令获取测试数据并保存为文件
        testCommand.append("curl ")
                .append("-o ");

        String caseName = testCase.getCaseName();

        if (StringUtils.isEmpty(caseName)) {
            caseName = "测试用例无测试名称";
        }

        testCommand.append(caseName)
                .append(".")
                .append(commandRunCaseSuffix)
                .append(" ${baseUrl}/case/data/")
                .append(testCase.getId())
                .append(" -H \"token: ${token}\" ");

        //本行命令执行失败，继续运行下面的命令行
        testCommand.append(" || true");

        testCommand.append("\n");
    }
}
