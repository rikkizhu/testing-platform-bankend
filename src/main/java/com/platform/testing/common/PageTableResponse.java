package com.platform.testing.common;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @program: PageTableResponse
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-17 16:22
 **/

@Data
public class PageTableResponse<T> implements Serializable {
    private static final long serialVersionUID = -7472879865481412372L;

    //返回的数据量
    private Integer recordsTotal;

    //返回的数据列表
    private List<T> data;
}
