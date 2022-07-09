package com.platform.testing.dao;

import com.platform.testing.common.MySqlExtensionMapper;
import com.platform.testing.entity.TestJenkins;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TestJenkinsMapper extends MySqlExtensionMapper<TestJenkins> {
    int count(@Param("params") Map params);

    List<TestJenkins> list(@Param("params") Map params,
                           @Param("pageNum") Integer pageNum,
                           @Param("pageSize") Integer pageSize);
}