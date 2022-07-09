package com.platform.testing.service.impl;

import com.platform.testing.common.*;
import com.platform.testing.dao.TestJenkinsMapper;
import com.platform.testing.dao.TestUserMapper;
import com.platform.testing.dto.jenkins.QueryTestJenkinsListDto;
import com.platform.testing.entity.TestJenkins;
import com.platform.testing.entity.TestUser;
import com.platform.testing.service.TestJenkinsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: TestJenkinsServiceImpl
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-20 12:39
 **/

@Slf4j
@Service
public class TestJenkinsServiceImpl implements TestJenkinsService {

    @Autowired
    private TestJenkinsMapper testJenkinsMapper;

    @Autowired
    private TestUserMapper testUserMapper;
    @Autowired
    private TokenDb tokenDb;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultDto<TestJenkins> save(TokenDto tokenDto, TestJenkins testJenkins) {
        testJenkins.setCreateTime(new Date());
        testJenkins.setUpdateTime(new Date());
        testJenkinsMapper.insertUseGeneratedKeys(testJenkins);

        //如果是否为默认Jenkins的标志位为1，则修改test_user表中的default_jenkins_id为1
        Integer defaultJenkinsFlag = testJenkins.getDefaultJenkinsFlag();

        if (Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag == 1) {
            //从tokenDb中获取用户信息（包含用户id）
            Integer createUserId = testJenkins.getCreateUserId();
            TestUser testUser = new TestUser();
            testUser.setId(createUserId);
            TestUser resultTestUser = testUserMapper.selectOne(testUser);

            if (Objects.isNull(resultTestUser)) {
                return ResultDto.fail("用户未找到");
            }
            //将新增的JenkinsId放入default_jenkins_id字段，并根据用户id更新test_user
            resultTestUser.setDefaultJenkinsId(testJenkins.getId());
            //更新语句
            testUserMapper.updateByPrimaryKeySelective(resultTestUser);

            //更新token信息中的默认JenkinsId
            tokenDto.setDefaultJenkinsId(testJenkins.getId());
            tokenDb.addUserInfo(tokenDto.getToken(), tokenDto);
        }


        return ResultDto.success("成功", testJenkins);
    }

    @Override
    public ResultDto<PageTableResponse<TestJenkins>> list(PageTableRequest<QueryTestJenkinsListDto> pageTableRequest) {

        //获取参数
        Integer pageNum = pageTableRequest.getPageNum();
        Integer pageSize = pageTableRequest.getPageSize();
        Map params = pageTableRequest.getParams();
        Integer createUserId = (Integer) params.get("createUserId");

        TestUser queryTestUser = new TestUser();
        queryTestUser.setId(createUserId);
        TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);
        Integer defaultJenkinsId = resultTestUser.getDefaultJenkinsId();

        //获取数据总数量
        int count = testJenkinsMapper.count(params);

        //获取数据列表
        List<TestJenkins> testJenkinsList = testJenkinsMapper
                .list(params, (pageNum - 1) * pageSize, pageSize);

        //查找默认Jenkins
        for (TestJenkins testJenkins:testJenkinsList) {
            if(testJenkins.getId().equals(defaultJenkinsId)){
                testJenkins.setDefaultJenkinsFlag(1);
            }
        }

        //给响应参数赋值
        PageTableResponse response = new PageTableResponse();
        response.setRecordsTotal(count);
        response.setData(testJenkinsList);

        //返回
        return ResultDto.success("成功", response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TestJenkins> update(TokenDto tokenDto, TestJenkins testJenkins) {
        TestJenkins queryTestJenkins = new TestJenkins();

        queryTestJenkins.setId(testJenkins.getId());
        queryTestJenkins.setCreateUserId(testJenkins.getCreateUserId());

        TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

        if (Objects.isNull(result)) {
            ResultDto.fail("未查到jenkins信息");
        }

        testJenkins.setCreateTime(result.getCreateTime());
        testJenkins.setUpdateTime(new Date());

        testJenkinsMapper.updateByPrimaryKey(testJenkins);

        Integer defaultJenkinsFlag = testJenkins.getDefaultJenkinsFlag();

        if (Objects.nonNull(defaultJenkinsFlag) && defaultJenkinsFlag == 1) {
            Integer createUserId = testJenkins.getCreateUserId();
            TestUser testUser = new TestUser();
            testUser.setId(createUserId);
            testJenkins.setDefaultJenkinsFlag(testJenkins.getId());

            tokenDto.setDefaultJenkinsId(testJenkins.getId());
            tokenDb.addUserInfo(tokenDto.getToken(), tokenDto);

            testUserMapper.updateByPrimaryKeySelective(testUser);
        }

        return ResultDto.success("成功");
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TestJenkins> delete(Integer jenkinsId, TokenDto tokenDto) {
        TestJenkins queryTestJenkins = new TestJenkins();

        queryTestJenkins.setId(jenkinsId);
        queryTestJenkins.setCreateUserId(tokenDto.getUserId());

        TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

        if (Objects.isNull(result)) {
            return ResultDto.fail("未查到Jenkins信息");
        }

        TestUser queryTestUser = new TestUser();
        queryTestUser.setId(tokenDto.getUserId());

        TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

        Integer defaultJenkinsId = resultTestUser.getDefaultJenkinsId();

        if (Objects.nonNull(defaultJenkinsId) && defaultJenkinsId.equals(jenkinsId)) {
            TestUser testUser = new TestUser();
            testUser.setId(tokenDto.getUserId());
            testUser.setDefaultJenkinsId(null);

            tokenDto.setDefaultJenkinsId(null);
            tokenDb.addUserInfo(tokenDto.getToken(), tokenDto);

            testUserMapper.updateByPrimaryKeySelective(testUser);
        }

        testJenkinsMapper.deleteByPrimaryKey(jenkinsId);

        return ResultDto.success("成功");

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultDto<TestJenkins> getById(Integer jenkinsId, Integer createUserId) {
        TestJenkins queryTestJenkins = new TestJenkins();

        queryTestJenkins.setId(jenkinsId);
        queryTestJenkins.setCreateUserId(createUserId);

        TestJenkins result = testJenkinsMapper.selectOne(queryTestJenkins);

        if (Objects.isNull(result)) {
            ResultDto.fail("未查到Jenkins信息");
        }

        TestUser queryTestUser = new TestUser();
        queryTestUser.setId(createUserId);

        TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);
        Integer defaultJenkinsId = resultTestUser.getDefaultJenkinsId();

        if (result.getId().equals(defaultJenkinsId)) {
            result.setDefaultJenkinsFlag(1);
        }

        return ResultDto.success("成功", result);
    }


}
