package com.platform.testing.controller;

import com.platform.testing.service.UserService;

/**
 * @program: UserController
 * @description:
 * @author: zhuruiqi
 * @create: 2022-05-31 20:49
 **/
public class UserController {

    private UserService userService;

    public   UserService getUserService(){
        return userService;
    }

    public void setUserService(UserService userService){
        this.userService=userService;
    }

}
