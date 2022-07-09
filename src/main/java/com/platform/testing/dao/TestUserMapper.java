package com.platform.testing.dao;

import com.platform.testing.common.MySqlExtensionMapper;
import com.platform.testing.entity.TestUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestUserMapper extends MySqlExtensionMapper<TestUser> {

//    List<TestUser> selectTestUser(@Param("id") Integer id);

    List<TestUser> getByName(@Param("userName") String userName, @Param("userId") Integer userId);


}