package com.platform.testing.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @program: MySqlExtensionMapper
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-17 21:32
 **/
public interface MySqlExtensionMapper<T> extends Mapper<T>, MySqlMapper<T>, IdsMapper<T> {
}
