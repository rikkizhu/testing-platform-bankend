package com.platform.testing.service;

import com.platform.testing.common.ResultDto;
import com.platform.testing.dto.UserDto;
import com.platform.testing.entity.TestUser;

import java.util.List;

/**
 * @program: TestUserService
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-16 10:10
 **/
public interface TestUserService {
     ResultDto login(UserDto userDto);

     ResultDto<TestUser> save(TestUser testUser);

     ResultDto<List<TestUser>> getByName(TestUser testUser);

     ResultDto delete(Integer userId);



}
