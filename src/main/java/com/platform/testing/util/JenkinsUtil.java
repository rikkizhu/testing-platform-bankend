package com.platform.testing.util;

import com.alibaba.fastjson.JSONObject;
import com.platform.testing.common.ResultDto;
import com.platform.testing.common.UserBaseStr;
import com.platform.testing.dto.RequestInfoDto;
import com.platform.testing.dto.jenkins.OperateJenkinsJobDto;
import com.platform.testing.entity.TestJenkins;
import com.platform.testing.entity.TestTask;
import com.platform.testing.entity.TestUser;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.client.JenkinsHttpClient;
import com.offbytwo.jenkins.model.Job;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: JenkinsUtil
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-18 22:12
 **/
public class JenkinsUtil {
    public static void build(String jobName, String userId, String remark, String testCommand) throws IOException, URISyntaxException {
        ClassPathResource classPathResource = new ClassPathResource("JenkinsConfigDir/test_jenkins_show.xml");
        InputStream inputStream = classPathResource.getInputStream();
        String jobConfigXml = IOUtils.toString(inputStream, StandardCharsets.UTF_8);


        String baseUrl = "http://localhost:8080/";
        String userName = "admin";
        String password = "admin";

        JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(baseUrl), userName, password);
        String jenkinsVersion = jenkinsHttpClient.getJenkinsVersion();
        System.out.println(jenkinsVersion);

        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);

        jobName = "test19";
//        jenkinsServer.createJob(jobName,jobConfigXml,true);
        jenkinsServer.updateJob(jobName, jobConfigXml, true);


        Map<String, Job> jobMap = jenkinsServer.getJobs();
        Job job = jobMap.get("test19");
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("remark", remark);
        map.put("testCommand", testCommand);
        job.build(map, true);
    }

    public static ResultDto<TestUser> build2(OperateJenkinsJobDto operateJenkinsJobDto) throws IOException, URISyntaxException {
        ClassPathResource classPathResource = new ClassPathResource("JenkinsConfigDir/test_mini_test_start.xml");
        InputStream inputStream = classPathResource.getInputStream();
        String jobConfigXml = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

        TestJenkins testJenkins = operateJenkinsJobDto.getTestJenkins();
        TestUser testUser = operateJenkinsJobDto.getTestUser();
        Map<String, String> params = operateJenkinsJobDto.getParams();

        String baseUrl = testJenkins.getUrl();
        String userName = testJenkins.getUserName();
        String password = testJenkins.getPassword();

        String jobName = "test_mini_test_start_" + testUser.getId();

        JenkinsHttpClient jenkinsHttpClient = new JenkinsHttpClient(new URI(baseUrl), userName, password);
//        String jenkinsVersion = jenkinsHttpClient.getJenkinsVersion();


        JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHttpClient);
        if (StringUtils.isEmpty(testUser.getStartTestJobName())) {
            jenkinsServer.createJob(jobName, jobConfigXml, true);
            testUser.setStartTestJobName(jobName);
        } else {
            jenkinsServer.updateJob(jobName, jobConfigXml, true);

        }

        Map<String, Job> jobMap = jenkinsServer.getJobs();
        Job job = jobMap.get(jobName);

        job.build(params, true);

        return ResultDto.success("成功", testUser);
    }

    public static StringBuilder getUpdateTaskStatusUrl(RequestInfoDto requestInfoDto, TestTask testTask) {

        StringBuilder updateStatusUrl = new StringBuilder();

        updateStatusUrl.append("curl -X POST ");
        updateStatusUrl.append("\"" + requestInfoDto.getBaseUrl() + "/task/status \" ");
        updateStatusUrl.append("-H \"Content-Type: application/json \" ");
        updateStatusUrl.append("-H \"token: " + requestInfoDto.getToken() + "\" ");
        updateStatusUrl.append("-d ");
        JSONObject json = new JSONObject();

        json.put("taskId", testTask.getId());
        json.put("status", UserBaseStr.STATUS_DONE);
        json.put("buildUrl", "${BUILD_URL}");

        updateStatusUrl.append("'" + json.toJSONString() + "'");

        return updateStatusUrl;
    }

}
