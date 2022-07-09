package com.platform.testing.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @program: DemoInterceptor
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-17 14:26
 **/

@Component
@Slf4j
public class DemoInterceptor implements HandlerInterceptor {
    @Autowired
    private TokenDb tokenDb;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("=== preHandle ===");
        log.info("=== request.getRequestURI() ===" + request.getRequestURI());

        //1、从请求的Header中获取客户端附加的token
        String token = request.getHeader("token");
        String uri=request.getRequestURI();

        if ("/testUser/login".equals(uri)||"/testUser/register".equals(uri) ){
            return true;
        }

        //2、如果请求中无token，响应码设401，抛出业务异常：客户端没有传token
        if (Objects.isNull(token)||token==""){
            ServiceException.throwEx("客户端未传token");
        }

        //3、从token中根据token查询TokenDto
        boolean loginFlag = tokenDb.isLogin(token);

        //如果为空，则响应码设置401,抛出业务异常
        //用户未登录
        if (!loginFlag){
            ServiceException.throwEx("用户未登录");
        }


        //否则，则允许通过


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        log.info("=== postHandle ===");
        log.info("=== request.getRequestURI() ===" + request.getRequestURI());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        log.info("=== afterCompletion ===");
        log.info("=== request.getRequestURI() ===" + request.getRequestURI());
    }
}
