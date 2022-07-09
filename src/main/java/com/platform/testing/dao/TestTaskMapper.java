package com.platform.testing.dao;

import com.platform.testing.common.MySqlExtensionMapper;
import com.platform.testing.dto.task.TaskDataDto;
import com.platform.testing.entity.TestTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TestTaskMapper extends MySqlExtensionMapper<TestTask> {
    int count(@Param("params") Map params);

    List<TestTask> list(@Param("params") Map params,
                        @Param("pageNum") Integer pageNum,
                        @Param("pageSize") Integer pageSize);

    List<TaskDataDto> getTaskByType(@Param("createUserId") Integer createUserId);

    List<TaskDataDto> getTaskByStatus(@Param("createUserId") Integer createUserId);

    List<TestTask> getCaseCountByTask(@Param("createUserId") Integer createUserId,
                                      @Param("start") Integer start,
                                      @Param("end") Integer end);
}