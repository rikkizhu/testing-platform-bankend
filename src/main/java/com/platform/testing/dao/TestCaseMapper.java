package com.platform.testing.dao;

import com.platform.testing.common.MySqlExtensionMapper;
import com.platform.testing.entity.TestCase;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TestCaseMapper extends MySqlExtensionMapper<TestCase> {
    /**
     * 统计总数
     * @param params
     * @return
     */
    int count(@Param("params") Map params);

    /**
     * 列表分页查询
     * @param params
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<TestCase> list(@Param("params") Map params,
                        @Param("pageNum") Integer pageNum,
                        @Param("pageSize") Integer pageSize);
}