package com.platform.testing.common;

/**
 * @program: PageTableArgumentResolver
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-20 16:57
 **/

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 分页、查询参数解析
 */
public class PageTableArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        Class<?> cla = parameter.getParameterType();
        return cla.isAssignableFrom(PageTableRequest.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        Map<String, String[]> param = request.getParameterMap();
        PageTableRequest tableRequest = new PageTableRequest();
        if (param.containsKey("pageNum")) {
            tableRequest.setPageNum(Integer.parseInt(request.getParameter("pageNum")));
        }
        if (param.containsKey("pageSize")) {
            tableRequest.setPageSize(Integer.parseInt(request.getParameter("pageSize")));
        }

        Map<String,Object> map = new HashMap<>();
        tableRequest.setParams(map);

        param.forEach((k, v) -> {
            if (v.length == 1) {
                map.put(k, v[0]);
            } else {
                map.put(k, Arrays.asList(v));
            }
        });


        return tableRequest;
    }
}
