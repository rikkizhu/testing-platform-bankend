package com.platform.testing.util;

import io.swagger.models.auth.In;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @program: StrUtil
 * @description:
 * @author: zhuruiqi
 * @create: 2022-03-21 21:38
 **/
public class StrUtil {
    /**
     * 转换前：[2,3,4,5]
     * 转换后：2,3,4,5
     *
     * @param caseIdList
     * @return
     */
    public static String list2IdsStr(List<Integer> caseIdList) {
        if (Objects.isNull(caseIdList)) {
            return null;
        }

        return caseIdList.toString()
                .replace("[", "")
                .replace("]", "");
    }


    public static String getHostAndPort(String requestUrl) {

        if (StringUtils.isEmpty(requestUrl)) {
            return "";
        }

        String http = "";
        String tempUrl = "";
        if (requestUrl.contains("://")) {
            http = requestUrl.substring(0, requestUrl.indexOf("://") + 3);
            tempUrl = requestUrl.substring(requestUrl.indexOf("://") + 3);
        }
        if (tempUrl.contains("/")) {
            tempUrl = tempUrl.substring(0, tempUrl.indexOf("/"));
        }
        return http + tempUrl;
    }
}
