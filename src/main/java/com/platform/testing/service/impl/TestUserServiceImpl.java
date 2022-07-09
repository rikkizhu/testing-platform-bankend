package com.platform.testing.service.impl;

import com.platform.testing.common.*;
import com.platform.testing.dao.TestUserMapper;
import com.platform.testing.dto.UserDto;
import com.platform.testing.entity.TestUser;
import com.platform.testing.service.TestUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @program: TestUserServiceImpl
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-16 10:11
 **/

@Slf4j
@Service
public class TestUserServiceImpl implements TestUserService {
    @Autowired
    private TestUserMapper testUserMapper;

    @Autowired
    private TokenDb tokenDb;

    @Override
    public ResultDto login(UserDto userDto) {

        TestUser queryTestUser = new TestUser();

        //1、获取用户录入的用户名/密码并用MD5加密
        String newPwd = DigestUtils.md5DigestAsHex((UserBaseStr.md5Hex_sign + userDto.getName() + userDto.getPwd()).getBytes());
        queryTestUser.setUserName(userDto.getName());
        queryTestUser.setPassword(newPwd);

        //2、根据用户名+密码查询数据库中是否存在数据
        TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);

        //3、若不存在提示，用户不存在或密码错误
        if (Objects.isNull(resultTestUser)){
            return ResultDto.fail("用户不存在或密码错误");
        }
        //4、若存在，则创建token对象，生成token并将相关信息存入TokenDto
        String tokenStr = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + userDto.getName() + userDto.getPwd()).getBytes());
        TokenDto tokenDto = new TokenDto();
        tokenDto.setUserId(resultTestUser.getId());
        tokenDto.setUserName(resultTestUser.getUserName());
        tokenDto.setDefaultJenkinsId(resultTestUser.getDefaultJenkinsId());
        tokenDto.setToken(tokenStr);

        tokenDb.addUserInfo(tokenStr,tokenDto);

        MyToken myToken = new MyToken();
        myToken.setToken(tokenStr);

        return ResultDto.success("成功", myToken);
    }

    @Override
    public ResultDto<TestUser> save(TestUser testUser) {
        testUser.setCreateTime(new Date());
        testUser.setUpdateTime(new Date());
        //1、校验用户名是否已经存在
        TestUser queryTestUser = new TestUser();
        queryTestUser.setUserName(testUser.getUserName());
        TestUser resultTestUser = testUserMapper.selectOne(queryTestUser);
        if (Objects.nonNull(resultTestUser)) {
            return ResultDto.fail("用户名已存在");
        }

        //2、密码MD5加密存储
        String newPwd = DigestUtils.md5DigestAsHex((UserBaseStr.md5Hex_sign + testUser.getUserName() + testUser.getPassword()).getBytes());
        testUser.setPassword(newPwd);

        testUserMapper.insertUseGeneratedKeys(testUser);
        testUser.setPassword(null);
        return ResultDto.success("成功", testUser);
    }

    @Override
    public ResultDto<List<TestUser>> getByName(TestUser testUser) {
//        List<TestUser> testUsersList = testUserMapper.getByName(testUser.getUserName(), testUser.getId());
        List<TestUser> testUsersList = testUserMapper.select(testUser);
        return ResultDto.success("成功", testUsersList);
    }

    @Override
    public ResultDto delete(Integer userId) {
        TestUser testUser = new TestUser();
        testUser.setId(userId);
        testUserMapper.delete(testUser);
        return ResultDto.success("成功");
    }
}
