package com.platform.testing.common;

/**
 * @program: UserBaseStr
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-19 19:50
 **/
public class UserBaseStr {
    public static final String LOGIN_TOKEN = "token";
    public static final String md5Hex_sign = "RikkyTest";

    /**
     * 任务类型  1 执行测试任务 2 一键执行测试的任务
     */
    public static final Integer TASK_TYPE_EXECUTE = 1;
    public static final Integer TASK_TYPE_ONECLICK = 2;

    /**
     * 状态 0无效 1新建 2执行中 3执行完成
     */
    public static final Integer STATUS_INVALID = 0;
    public static final Integer STATUS_CREATE = 1;
    public static final Integer STATUS_TESTING = 2;
    public static final Integer STATUS_DONE = 3;

    /**
     * 命令运行的測试用例类型 1文本 2文件
     */
    public static final Integer CASE_TYPE_TEXT = 1;
    public static final Integer CASE_TYPE_FILE = 2;

}
