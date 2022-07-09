package com.platform.testing.controller;

import com.alibaba.fastjson.JSONObject;
import com.platform.testing.common.*;
import com.platform.testing.dto.AddTestUserDto;
import com.platform.testing.dto.BuildDto;
import com.platform.testing.dto.UserDto;
import com.platform.testing.entity.TestUser;
import com.platform.testing.service.TestUserService;
import com.platform.testing.util.JenkinsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * @program: TestUserController
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-15 16:49
 **/

@Api(tags = "用户管理模块")
@RestController
@RequestMapping("testUser")
@Slf4j
public class TestUserController {
    @Value("${rikky.key1}")
    private String rikkyKey1;

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private TokenDb tokenDb;


    @ApiOperation("登录接口")
    @PostMapping("login")
    public ResultDto login(@RequestBody UserDto userDto) {
        ResultDto result = testUserService.login(userDto);

//        if (userDto.getName().contains("error2")){
//            throw new ServiceException("error2",new NullPointerException());
//        }

//        try {
        if (userDto.getName().contains("error2")) {
            throw new NullPointerException();
        }

        if (userDto.getName().contains("error")) {
            ServiceException.throwEx("用户名中含有error");
        }
//        }catch (Exception e){
//            log.error(e.getMessage());
//            return "系统繁忙，请稍后重试";
//        }

        return testUserService.login(userDto);
    }


    @GetMapping("isLogin")
    public ResultDto isLogin(HttpServletRequest request) {
        //1、从请求的Header获取客户端附加token
        String tokenStr = request.getHeader(UserBaseStr.LOGIN_TOKEN);
        TokenDto tokenDto = tokenDb.getUserInfo(tokenStr);
        return ResultDto.success("成功", tokenDto);
    }

    @ApiOperation(value = "登出接口")
    @DeleteMapping("/logout")
    public ResultDto logout(HttpServletRequest request) {

        String token = request.getHeader(UserBaseStr.LOGIN_TOKEN);
        boolean loginFlag = tokenDb.isLogin(token);

        if(!loginFlag){
            return ResultDto.fail("用户未登录，无需退出");
        }

        TokenDto tokenDto = tokenDb.removeTokenDto(token);

        return ResultDto.success("成功",tokenDto);
    }


    @ApiOperation("用户注册")
    @PostMapping("register")
    public ResultDto<TestUser> register(@RequestBody AddTestUserDto addTestUserDto) {
        TestUser testUser = new TestUser();
        BeanUtils.copyProperties(addTestUserDto, testUser);

        if (StringUtils.isEmpty(addTestUserDto.getUserName())) {
            return ResultDto.fail("用户名不能为空");
        }
        if (StringUtils.isEmpty(addTestUserDto.getPassword())) {
            return ResultDto.fail("密码不能为空");
        }

        log.info("用户注册 请求入参" + JSONObject.toJSONString(testUser));
        return testUserService.save(testUser);
    }

    @GetMapping(value = "byName")
    public ResultDto<List<TestUser>> getByName(@RequestParam(value = "userId", required = false) Integer userId, @RequestParam(value = "userName", required = false) String userName) {
        log.info("根据用户名模糊查询入参" + userName);
        TestUser testUser = new TestUser();
        testUser.setId(userId);
        testUser.setUserName(userName);

        log.info("用户注册 请求入参" + JSONObject.toJSONString(testUser));
        return testUserService.getByName(testUser);
    }

    @DeleteMapping(value = "{userId}")
    public ResultDto delete(@PathVariable("userId") Integer userId) {
        System.out.println("根据用户id删除用户信息" + userId);

        return testUserService.delete(userId);
    }


//    //    @RequestMapping(value = "login", method = RequestMethod.POST)
//    @PostMapping("login")
//    public String login(@RequestBody UserDto userDto) {
//        System.out.println(userDto.getName());
//        System.out.println(userDto.getPwd());
//        return "success";
//    }


    @RequestMapping(value = "byId/{userId}/{id}", method = RequestMethod.GET)
    public String getById(@PathVariable("userId") Long userId, @PathVariable("id") long id) {
        System.out.println(userId);
        System.out.println(id);
        return "success " + userId + " " + id;
    }

    @RequestMapping(value = "byId", method = RequestMethod.GET)
    public String getById2(@RequestParam(value = "userId", required = false) Long userId, @RequestParam("id") long id) {
        System.out.println(userId);
        System.out.println(id);
        return "success " + userId + " " + id;
    }

    @PostMapping(value = "build")
    public ResultDto build(@RequestBody BuildDto buildDto) throws IOException, URISyntaxException {
        log.info("调用jenkins构建Job 请求入参" + JSONObject.toJSONString(buildDto));
        JenkinsUtil.build(buildDto.getJobName(), buildDto.getUserId(), buildDto.getRemark(), buildDto.getTestCommand());
        return ResultDto.success("成功");
    }


}
